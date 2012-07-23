/**
 * Party_GetMostRecentItemsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetMostRecentItemsResponse  implements java.io.Serializable {
    private String party_GetMostRecentItemsResult;

    public Party_GetMostRecentItemsResponse() {
    }

    public Party_GetMostRecentItemsResponse(
           String party_GetMostRecentItemsResult) {
           this.party_GetMostRecentItemsResult = party_GetMostRecentItemsResult;
    }


    /**
     * Gets the party_GetMostRecentItemsResult value for this Party_GetMostRecentItemsResponse.
     * 
     * @return party_GetMostRecentItemsResult
     */
    public String getParty_GetMostRecentItemsResult() {
        return party_GetMostRecentItemsResult;
    }


    /**
     * Sets the party_GetMostRecentItemsResult value for this Party_GetMostRecentItemsResponse.
     * 
     * @param party_GetMostRecentItemsResult
     */
    public void setParty_GetMostRecentItemsResult(String party_GetMostRecentItemsResult) {
        this.party_GetMostRecentItemsResult = party_GetMostRecentItemsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetMostRecentItemsResponse)) return false;
        Party_GetMostRecentItemsResponse other = (Party_GetMostRecentItemsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetMostRecentItemsResult==null && other.getParty_GetMostRecentItemsResult()==null) || 
             (this.party_GetMostRecentItemsResult!=null &&
              this.party_GetMostRecentItemsResult.equals(other.getParty_GetMostRecentItemsResult())));
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
        if (getParty_GetMostRecentItemsResult() != null) {
            _hashCode += getParty_GetMostRecentItemsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetMostRecentItemsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetMostRecentItemsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetMostRecentItemsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetMostRecentItemsResult"));
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
