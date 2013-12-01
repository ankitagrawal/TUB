/**
 * Party_GetConsumersMatchingWithRuleResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetConsumersMatchingWithRuleResponse  implements java.io.Serializable {
    private String party_GetConsumersMatchingWithRuleResult;

    public Party_GetConsumersMatchingWithRuleResponse() {
    }

    public Party_GetConsumersMatchingWithRuleResponse(
           String party_GetConsumersMatchingWithRuleResult) {
           this.party_GetConsumersMatchingWithRuleResult = party_GetConsumersMatchingWithRuleResult;
    }


    /**
     * Gets the party_GetConsumersMatchingWithRuleResult value for this Party_GetConsumersMatchingWithRuleResponse.
     * 
     * @return party_GetConsumersMatchingWithRuleResult
     */
    public String getParty_GetConsumersMatchingWithRuleResult() {
        return party_GetConsumersMatchingWithRuleResult;
    }


    /**
     * Sets the party_GetConsumersMatchingWithRuleResult value for this Party_GetConsumersMatchingWithRuleResponse.
     * 
     * @param party_GetConsumersMatchingWithRuleResult
     */
    public void setParty_GetConsumersMatchingWithRuleResult(String party_GetConsumersMatchingWithRuleResult) {
        this.party_GetConsumersMatchingWithRuleResult = party_GetConsumersMatchingWithRuleResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetConsumersMatchingWithRuleResponse)) return false;
        Party_GetConsumersMatchingWithRuleResponse other = (Party_GetConsumersMatchingWithRuleResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.party_GetConsumersMatchingWithRuleResult==null && other.getParty_GetConsumersMatchingWithRuleResult()==null) || 
             (this.party_GetConsumersMatchingWithRuleResult!=null &&
              this.party_GetConsumersMatchingWithRuleResult.equals(other.getParty_GetConsumersMatchingWithRuleResult())));
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
        if (getParty_GetConsumersMatchingWithRuleResult() != null) {
            _hashCode += getParty_GetConsumersMatchingWithRuleResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetConsumersMatchingWithRuleResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetConsumersMatchingWithRuleResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("party_GetConsumersMatchingWithRuleResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Party_GetConsumersMatchingWithRuleResult"));
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
