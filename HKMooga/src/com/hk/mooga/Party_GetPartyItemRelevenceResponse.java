/**
 * Party_GetPartyItemRelevenceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetPartyItemRelevenceResponse  implements java.io.Serializable {
    private String party_GetPartyItemRelevenceResult;

    public Party_GetPartyItemRelevenceResponse() {
    }

    public Party_GetPartyItemRelevenceResponse(
           String party_GetPartyItemRelevenceResult) {
           this.party_GetPartyItemRelevenceResult = party_GetPartyItemRelevenceResult;
    }


    /**
     * Gets the party_GetPartyItemRelevenceResult value for this Party_GetPartyItemRelevenceResponse.
     * 
     * @return party_GetPartyItemRelevenceResult
     */
    public String getParty_GetPartyItemRelevenceResult() {
        return party_GetPartyItemRelevenceResult;
    }


    /**
     * Sets the party_GetPartyItemRelevenceResult value for this Party_GetPartyItemRelevenceResponse.
     * 
     * @param party_GetPartyItemRelevenceResult
     */
    public void setParty_GetPartyItemRelevenceResult(String party_GetPartyItemRelevenceResult) {
        this.party_GetPartyItemRelevenceResult = party_GetPartyItemRelevenceResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetPartyItemRelevenceResponse)) return false;
        Party_GetPartyItemRelevenceResponse other = (Party_GetPartyItemRelevenceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetPartyItemRelevenceResult==null && other.getParty_GetPartyItemRelevenceResult()==null) || 
             (this.party_GetPartyItemRelevenceResult!=null &&
              this.party_GetPartyItemRelevenceResult.equals(other.getParty_GetPartyItemRelevenceResult())));
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
        if (getParty_GetPartyItemRelevenceResult() != null) {
            _hashCode += getParty_GetPartyItemRelevenceResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetPartyItemRelevenceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetPartyItemRelevenceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetPartyItemRelevenceResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetPartyItemRelevenceResult"));
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
