package com.linkedin.databus.client;

import java.util.List;

import javax.management.MBeanServer;

import com.linkedin.databus.client.DatabusSourcesConnection.StaticConfig;
import com.linkedin.databus.client.consumer.MultiConsumerCallback;
import com.linkedin.databus.client.pub.CheckpointPersistenceProvider;
import com.linkedin.databus.client.pub.DatabusCombinedConsumer;
import com.linkedin.databus.client.pub.RegistrationId;
import com.linkedin.databus.core.Checkpoint;
import com.linkedin.databus.core.DbusClientMode;
import com.linkedin.databus.core.DbusEvent;
import com.linkedin.databus.core.DbusEventUtils;
import com.linkedin.databus.core.DbusEventBuffer;
import com.linkedin.databus.core.SCNRegressMessage;
import com.linkedin.databus.core.async.LifecycleMessage;
import com.linkedin.databus.core.data_model.DatabusSubscription;

public class RelayDispatcher extends GenericDispatcher<DatabusCombinedConsumer>
{

  private final BootstrapPullThread _bootstrapPuller;

  public RelayDispatcher(String name,
                         StaticConfig connConfig,
                         List<DatabusSubscription> subsList,
                         CheckpointPersistenceProvider checkpointPersistor,
                         DbusEventBuffer dataEventsBuffer,
                         MultiConsumerCallback<DatabusCombinedConsumer> asyncCallback,
                         BootstrapPullThread bootstrapPuller,
                         MBeanServer mbeanServer,
                         DatabusHttpClientImpl serverHandle,
                         RegistrationId registrationId)
  {
    super(name, connConfig, subsList, checkpointPersistor, dataEventsBuffer, asyncCallback,mbeanServer,serverHandle, registrationId);
    _bootstrapPuller = bootstrapPuller;
  }

  @Override
  protected Checkpoint createCheckpoint(DispatcherState curState, DbusEvent event)
  {
    Checkpoint cp = new Checkpoint();
    cp.setConsumptionMode(DbusClientMode.ONLINE_CONSUMPTION);
    cp.setWindowScn(event.sequence());
    cp.setWindowOffset(-1);

    return cp;
  }

  @Override
  protected boolean processSysEvent(DispatcherState curState, DbusEvent event)
  {
    boolean success = true;

    if (event.isCheckpointMessage())
    {
      Checkpoint ckpt = null;
      try
      {
        ckpt = DbusEventUtils.getCheckpointFromEvent(event);
        DbusClientMode bootstrapMode = ckpt.getConsumptionMode();

        if (bootstrapMode != DbusClientMode.ONLINE_CONSUMPTION)
        {
          if(_bootstrapPuller == null) {
            LOG.error("checkpoint specifies bootstrap mode, but bootstrapPuller is null (boostrap disabled)");
            return false;
          }
          ckpt.setConsumptionMode(DbusClientMode.BOOTSTRAP_SNAPSHOT);
          if (curState.getStateId() != DispatcherState.StateId.EXPECT_EVENT_WINDOW)
          {
            LOG.warn("state before bootstrap: " + curState.getStateId());
            //Fixing bug that caused TestRelayBootstrapSwitch to fail; no apparent need to rollback
            //curState.switchToRollback();
            //doRollback(curState);
          }
          curState.getEventsIterator().getEventBuffer().clear();
          curState.resetIterators();
          curState.switchToExpectEventWindow();

          _bootstrapPuller.enqueueMessage(LifecycleMessage.createStartMessage());
          LOG.info("Notifying bootstrap puller to start boostraping");
        }
        else
        {
          success = super.processSysEvent(curState, event);
        }
      }
      catch (Exception e )
      {
        LOG.error(getName() + ": checkpoint deserialization failed", e);
        success = false;
      }
    }
    else if (event.isSCNRegressMessage())
    {
    	SCNRegressMessage message = DbusEventUtils.getSCNRegressFromEvent(event);
    	LOG.warn("RelayDispatcher acting on SCNRegress : " + message);
    	curState.setSCNRegress(true);
        curState.switchToExpectEventWindow();
        enqueueMessage(curState);
    }
    else
    {
      success = super.processSysEvent(curState, event);
    }

    return success;
  }

}
