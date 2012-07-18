/**
 * Party_GetConsumersForItemResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetConsumersForItemResponse  implements java.io.Serializable {
    private String party_GetConsumersForItemResult;

    public Party_GetConsumersForItemResponse() {
    }

    public Party_GetConsumersForItemResponse(
           String party_GetConsumersForItemResult) {
           this.party_GetConsumersForItemResult = party_GetConsumersForItemResult;
    }


    /**
     * Gets the party_GetConsumersForItemResult value for this Party_GetConsumersForItemResponse.
     * 
     * @return party_GetConsumersForItemResult
     */
    public String getParty_GetConsumersForItemResult() {
        return party_GetConsumersForItemResult;
    }


    /**
     * Sets the party_GetConsumersForItemResult value for this Party_GetConsumersForItemResponse.
     * 
     * @param party_GetConsumersForItemResult
     */
    public void setParty_GetConsumersForItemResult(String party_GetConsumersForItemResult) {
        this.party_GetConsumersForItemResult = party_GetConsumersForItemResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetConsumersForItemResponse)) return false;
        Party_GetConsumersForItemResponse other = (Party_GetConsumersForItemResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetConsumersForItemResult==null && other.getParty_GetConsumersForItemResult()==null) || 
             (this.party_GetConsumersForItemResult!=null &&
              this.party_GetConsumersForItemResult.equals(other.getParty_GetConsumersForItemResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getParty_GetConsumersForItemResult() != null) {
            _hashCode += getParty_GetConsumersForItemResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetConsumersForItemResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetConsumersForItemResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetConsumersForItemResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetConsumersForItemResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
