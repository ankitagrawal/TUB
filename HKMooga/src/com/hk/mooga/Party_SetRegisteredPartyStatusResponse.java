/**
 * Party_SetRegisteredPartyStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_SetRegisteredPartyStatusResponse  implements java.io.Serializable {
    private String party_SetRegisteredPartyStatusResult;

    public Party_SetRegisteredPartyStatusResponse() {
    }

    public Party_SetRegisteredPartyStatusResponse(
           String party_SetRegisteredPartyStatusResult) {
           this.party_SetRegisteredPartyStatusResult = party_SetRegisteredPartyStatusResult;
    }


    /**
     * Gets the party_SetRegisteredPartyStatusResult value for this Party_SetRegisteredPartyStatusResponse.
     * 
     * @return party_SetRegisteredPartyStatusResult
     */
    public String getParty_SetRegisteredPartyStatusResult() {
        return party_SetRegisteredPartyStatusResult;
    }


    /**
     * Sets the party_SetRegisteredPartyStatusResult value for this Party_SetRegisteredPartyStatusResponse.
     * 
     * @param party_SetRegisteredPartyStatusResult
     */
    public void setParty_SetRegisteredPartyStatusResult(String party_SetRegisteredPartyStatusResult) {
        this.party_SetRegisteredPartyStatusResult = party_SetRegisteredPartyStatusResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_SetRegisteredPartyStatusResponse)) return false;
        Party_SetRegisteredPartyStatusResponse other = (Party_SetRegisteredPartyStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_SetRegisteredPartyStatusResult==null && other.getParty_SetRegisteredPartyStatusResult()==null) || 
             (this.party_SetRegisteredPartyStatusResult!=null &&
              this.party_SetRegisteredPartyStatusResult.equals(other.getParty_SetRegisteredPartyStatusResult())));
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
        if (getParty_SetRegisteredPartyStatusResult() != null) {
            _hashCode += getParty_SetRegisteredPartyStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_SetRegisteredPartyStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_SetRegisteredPartyStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_SetRegisteredPartyStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_SetRegisteredPartyStatusResult"));
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
