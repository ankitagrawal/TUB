/**
 * Party_GetPreferredtemsToConsumerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetPreferredtemsToConsumerResponse  implements java.io.Serializable {
    private String party_GetPreferredtemsToConsumerResult;

    public Party_GetPreferredtemsToConsumerResponse() {
    }

    public Party_GetPreferredtemsToConsumerResponse(
           String party_GetPreferredtemsToConsumerResult) {
           this.party_GetPreferredtemsToConsumerResult = party_GetPreferredtemsToConsumerResult;
    }


    /**
     * Gets the party_GetPreferredtemsToConsumerResult value for this Party_GetPreferredtemsToConsumerResponse.
     * 
     * @return party_GetPreferredtemsToConsumerResult
     */
    public String getParty_GetPreferredtemsToConsumerResult() {
        return party_GetPreferredtemsToConsumerResult;
    }


    /**
     * Sets the party_GetPreferredtemsToConsumerResult value for this Party_GetPreferredtemsToConsumerResponse.
     * 
     * @param party_GetPreferredtemsToConsumerResult
     */
    public void setParty_GetPreferredtemsToConsumerResult(String party_GetPreferredtemsToConsumerResult) {
        this.party_GetPreferredtemsToConsumerResult = party_GetPreferredtemsToConsumerResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetPreferredtemsToConsumerResponse)) return false;
        Party_GetPreferredtemsToConsumerResponse other = (Party_GetPreferredtemsToConsumerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetPreferredtemsToConsumerResult==null && other.getParty_GetPreferredtemsToConsumerResult()==null) || 
             (this.party_GetPreferredtemsToConsumerResult!=null &&
              this.party_GetPreferredtemsToConsumerResult.equals(other.getParty_GetPreferredtemsToConsumerResult())));
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
        if (getParty_GetPreferredtemsToConsumerResult() != null) {
            _hashCode += getParty_GetPreferredtemsToConsumerResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetPreferredtemsToConsumerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetPreferredtemsToConsumerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetPreferredtemsToConsumerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetPreferredtemsToConsumerResult"));
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
