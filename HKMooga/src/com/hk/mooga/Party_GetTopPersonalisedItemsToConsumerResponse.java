/**
 * Party_GetTopPersonalisedItemsToConsumerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetTopPersonalisedItemsToConsumerResponse  implements java.io.Serializable {
    private String party_GetTopPersonalisedItemsToConsumerResult;

    public Party_GetTopPersonalisedItemsToConsumerResponse() {
    }

    public Party_GetTopPersonalisedItemsToConsumerResponse(
           String party_GetTopPersonalisedItemsToConsumerResult) {
           this.party_GetTopPersonalisedItemsToConsumerResult = party_GetTopPersonalisedItemsToConsumerResult;
    }


    /**
     * Gets the party_GetTopPersonalisedItemsToConsumerResult value for this Party_GetTopPersonalisedItemsToConsumerResponse.
     * 
     * @return party_GetTopPersonalisedItemsToConsumerResult
     */
    public String getParty_GetTopPersonalisedItemsToConsumerResult() {
        return party_GetTopPersonalisedItemsToConsumerResult;
    }


    /**
     * Sets the party_GetTopPersonalisedItemsToConsumerResult value for this Party_GetTopPersonalisedItemsToConsumerResponse.
     * 
     * @param party_GetTopPersonalisedItemsToConsumerResult
     */
    public void setParty_GetTopPersonalisedItemsToConsumerResult(String party_GetTopPersonalisedItemsToConsumerResult) {
        this.party_GetTopPersonalisedItemsToConsumerResult = party_GetTopPersonalisedItemsToConsumerResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetTopPersonalisedItemsToConsumerResponse)) return false;
        Party_GetTopPersonalisedItemsToConsumerResponse other = (Party_GetTopPersonalisedItemsToConsumerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetTopPersonalisedItemsToConsumerResult==null && other.getParty_GetTopPersonalisedItemsToConsumerResult()==null) || 
             (this.party_GetTopPersonalisedItemsToConsumerResult!=null &&
              this.party_GetTopPersonalisedItemsToConsumerResult.equals(other.getParty_GetTopPersonalisedItemsToConsumerResult())));
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
        if (getParty_GetTopPersonalisedItemsToConsumerResult() != null) {
            _hashCode += getParty_GetTopPersonalisedItemsToConsumerResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetTopPersonalisedItemsToConsumerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetTopPersonalisedItemsToConsumerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetTopPersonalisedItemsToConsumerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetTopPersonalisedItemsToConsumerResult"));
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
