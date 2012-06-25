/**
 * ExecuteServiceWithInternalIDsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.pact.service.mooga;

public class ExecuteServiceWithInternalIDsResponse  implements java.io.Serializable {
    private java.lang.String executeServiceWithInternalIDsResult;

    public ExecuteServiceWithInternalIDsResponse() {
    }

    public ExecuteServiceWithInternalIDsResponse(
           java.lang.String executeServiceWithInternalIDsResult) {
           this.executeServiceWithInternalIDsResult = executeServiceWithInternalIDsResult;
    }


    /**
     * Gets the executeServiceWithInternalIDsResult value for this ExecuteServiceWithInternalIDsResponse.
     * 
     * @return executeServiceWithInternalIDsResult
     */
    public java.lang.String getExecuteServiceWithInternalIDsResult() {
        return executeServiceWithInternalIDsResult;
    }


    /**
     * Sets the executeServiceWithInternalIDsResult value for this ExecuteServiceWithInternalIDsResponse.
     * 
     * @param executeServiceWithInternalIDsResult
     */
    public void setExecuteServiceWithInternalIDsResult(java.lang.String executeServiceWithInternalIDsResult) {
        this.executeServiceWithInternalIDsResult = executeServiceWithInternalIDsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExecuteServiceWithInternalIDsResponse)) return false;
        ExecuteServiceWithInternalIDsResponse other = (ExecuteServiceWithInternalIDsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.executeServiceWithInternalIDsResult==null && other.getExecuteServiceWithInternalIDsResult()==null) || 
             (this.executeServiceWithInternalIDsResult!=null &&
              this.executeServiceWithInternalIDsResult.equals(other.getExecuteServiceWithInternalIDsResult())));
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
        if (getExecuteServiceWithInternalIDsResult() != null) {
            _hashCode += getExecuteServiceWithInternalIDsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteServiceWithInternalIDsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteServiceWithInternalIDsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executeServiceWithInternalIDsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "ExecuteServiceWithInternalIDsResult"));
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
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
