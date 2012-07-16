/**
 * Party_GeneratePendingPersonalisedItemsToConsumerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GeneratePendingPersonalisedItemsToConsumerResponse  implements java.io.Serializable {
    private String party_GeneratePendingPersonalisedItemsToConsumerResult;

    public Party_GeneratePendingPersonalisedItemsToConsumerResponse() {
    }

    public Party_GeneratePendingPersonalisedItemsToConsumerResponse(
           String party_GeneratePendingPersonalisedItemsToConsumerResult) {
           this.party_GeneratePendingPersonalisedItemsToConsumerResult = party_GeneratePendingPersonalisedItemsToConsumerResult;
    }


    /**
     * Gets the party_GeneratePendingPersonalisedItemsToConsumerResult value for this Party_GeneratePendingPersonalisedItemsToConsumerResponse.
     * 
     * @return party_GeneratePendingPersonalisedItemsToConsumerResult
     */
    public String getParty_GeneratePendingPersonalisedItemsToConsumerResult() {
        return party_GeneratePendingPersonalisedItemsToConsumerResult;
    }


    /**
     * Sets the party_GeneratePendingPersonalisedItemsToConsumerResult value for this Party_GeneratePendingPersonalisedItemsToConsumerResponse.
     * 
     * @param party_GeneratePendingPersonalisedItemsToConsumerResult
     */
    public void setParty_GeneratePendingPersonalisedItemsToConsumerResult(String party_GeneratePendingPersonalisedItemsToConsumerResult) {
        this.party_GeneratePendingPersonalisedItemsToConsumerResult = party_GeneratePendingPersonalisedItemsToConsumerResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GeneratePendingPersonalisedItemsToConsumerResponse)) return false;
        Party_GeneratePendingPersonalisedItemsToConsumerResponse other = (Party_GeneratePendingPersonalisedItemsToConsumerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GeneratePendingPersonalisedItemsToConsumerResult==null && other.getParty_GeneratePendingPersonalisedItemsToConsumerResult()==null) || 
             (this.party_GeneratePendingPersonalisedItemsToConsumerResult!=null &&
              this.party_GeneratePendingPersonalisedItemsToConsumerResult.equals(other.getParty_GeneratePendingPersonalisedItemsToConsumerResult())));
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
        if (getParty_GeneratePendingPersonalisedItemsToConsumerResult() != null) {
            _hashCode += getParty_GeneratePendingPersonalisedItemsToConsumerResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GeneratePendingPersonalisedItemsToConsumerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GeneratePendingPersonalisedItemsToConsumerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GeneratePendingPersonalisedItemsToConsumerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GeneratePendingPersonalisedItemsToConsumerResult"));
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
