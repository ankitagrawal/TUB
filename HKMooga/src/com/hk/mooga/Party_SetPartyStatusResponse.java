/**
 * Party_SetPartyStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_SetPartyStatusResponse  implements java.io.Serializable {
    private String party_SetPartyStatusResult;

    public Party_SetPartyStatusResponse() {
    }

    public Party_SetPartyStatusResponse(
           String party_SetPartyStatusResult) {
           this.party_SetPartyStatusResult = party_SetPartyStatusResult;
    }


    /**
     * Gets the party_SetPartyStatusResult value for this Party_SetPartyStatusResponse.
     * 
     * @return party_SetPartyStatusResult
     */
    public String getParty_SetPartyStatusResult() {
        return party_SetPartyStatusResult;
    }


    /**
     * Sets the party_SetPartyStatusResult value for this Party_SetPartyStatusResponse.
     * 
     * @param party_SetPartyStatusResult
     */
    public void setParty_SetPartyStatusResult(String party_SetPartyStatusResult) {
        this.party_SetPartyStatusResult = party_SetPartyStatusResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_SetPartyStatusResponse)) return false;
        Party_SetPartyStatusResponse other = (Party_SetPartyStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_SetPartyStatusResult==null && other.getParty_SetPartyStatusResult()==null) || 
             (this.party_SetPartyStatusResult!=null &&
              this.party_SetPartyStatusResult.equals(other.getParty_SetPartyStatusResult())));
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
        if (getParty_SetPartyStatusResult() != null) {
            _hashCode += getParty_SetPartyStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_SetPartyStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_SetPartyStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_SetPartyStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_SetPartyStatusResult"));
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
