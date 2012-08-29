/**
 * Party_GetPersonalisedItemsToConsumerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetPersonalisedItemsToConsumerResponse  implements java.io.Serializable {
    private String party_GetPersonalisedItemsToConsumerResult;

    public Party_GetPersonalisedItemsToConsumerResponse() {
    }

    public Party_GetPersonalisedItemsToConsumerResponse(
           String party_GetPersonalisedItemsToConsumerResult) {
           this.party_GetPersonalisedItemsToConsumerResult = party_GetPersonalisedItemsToConsumerResult;
    }


    /**
     * Gets the party_GetPersonalisedItemsToConsumerResult value for this Party_GetPersonalisedItemsToConsumerResponse.
     * 
     * @return party_GetPersonalisedItemsToConsumerResult
     */
    public String getParty_GetPersonalisedItemsToConsumerResult() {
        return party_GetPersonalisedItemsToConsumerResult;
    }


    /**
     * Sets the party_GetPersonalisedItemsToConsumerResult value for this Party_GetPersonalisedItemsToConsumerResponse.
     * 
     * @param party_GetPersonalisedItemsToConsumerResult
     */
    public void setParty_GetPersonalisedItemsToConsumerResult(String party_GetPersonalisedItemsToConsumerResult) {
        this.party_GetPersonalisedItemsToConsumerResult = party_GetPersonalisedItemsToConsumerResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetPersonalisedItemsToConsumerResponse)) return false;
        Party_GetPersonalisedItemsToConsumerResponse other = (Party_GetPersonalisedItemsToConsumerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetPersonalisedItemsToConsumerResult==null && other.getParty_GetPersonalisedItemsToConsumerResult()==null) || 
             (this.party_GetPersonalisedItemsToConsumerResult!=null &&
              this.party_GetPersonalisedItemsToConsumerResult.equals(other.getParty_GetPersonalisedItemsToConsumerResult())));
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
        if (getParty_GetPersonalisedItemsToConsumerResult() != null) {
            _hashCode += getParty_GetPersonalisedItemsToConsumerResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetPersonalisedItemsToConsumerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetPersonalisedItemsToConsumerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetPersonalisedItemsToConsumerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetPersonalisedItemsToConsumerResult"));
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
