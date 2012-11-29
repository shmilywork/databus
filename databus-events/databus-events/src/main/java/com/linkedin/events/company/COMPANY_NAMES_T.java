/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.events.company;

@SuppressWarnings("all")
public class COMPANY_NAMES_T extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"COMPANY_NAMES_T\",\"namespace\":\"com.linkedin.events.company\",\"fields\":[{\"name\":\"companyNames\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"companyNameT\",\"fields\":[{\"name\":\"crudType\",\"type\":[\"string\",\"null\"],\"meta\":\"dbFieldName=CRUD_TYPE;dbFieldPosition=0;\"},{\"name\":\"companyNameId\",\"type\":[\"long\",\"null\"],\"meta\":\"dbFieldName=COMPANY_NAME_ID;dbFieldPosition=1;\"},{\"name\":\"companyId\",\"type\":[\"long\",\"null\"],\"meta\":\"dbFieldName=COMPANY_ID;dbFieldPosition=2;\"},{\"name\":\"name\",\"type\":[\"string\",\"null\"],\"meta\":\"dbFieldName=NAME;dbFieldPosition=3;\"},{\"name\":\"locale\",\"type\":[\"string\",\"null\"],\"meta\":\"dbFieldName=LOCALE;dbFieldPosition=4;\"},{\"name\":\"changesetId\",\"type\":[\"long\",\"null\"],\"meta\":\"dbFieldName=CHANGESET_ID;dbFieldPosition=5;\"},{\"name\":\"nameType\",\"type\":[\"string\",\"null\"],\"meta\":\"dbFieldName=NAME_TYPE;dbFieldPosition=6;\"},{\"name\":\"active\",\"type\":[\"string\",\"null\"],\"meta\":\"dbFieldName=ACTIVE;dbFieldPosition=7;\"}],\"meta\":\"dbFieldName=COMPANY_NAMES;dbFieldPosition=0;\"}}}]}");
  public java.util.List<com.linkedin.events.company.companyNameT> companyNames;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return companyNames;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: companyNames = (java.util.List<com.linkedin.events.company.companyNameT>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
}