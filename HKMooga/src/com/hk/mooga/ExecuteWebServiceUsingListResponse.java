/**
 * ExecuteWebServiceUsingListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteWebServiceUsingListResponse  implements java.io.Serializable {
    private String executeWebServiceUsingListResult;

    public ExecuteWebServiceUsingListResponse() {
    }

    public ExecuteWebServiceUsingListResponse(
           String executeWebServiceUsingListResult) {
           this.executeWebServiceUsingListResult = executeWebServiceUsingListResult;
    }


    /**
     * Gets the executeWebServiceUsingListResult value for this ExecuteWebServiceUsingListResponse.
     * 
     * @return executeWebServiceUsingListResult
     */
    public String getExecuteWebServiceUsingListResult() {
        return executeWebServiceUsingListResult;
    }


    /**
     * Sets the executeWebServiceUsingListResult value for this ExecuteWebServiceUsingListResponse.
     * 
     * @param executeWebServiceUsingListResult
     */
    public void setExecuteWebServiceUsingListResult(String executeWebServiceUsingListResult) {
        this.executeWebServiceUsingListResult = executeWebServiceUsingListResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteWebServiceUsingListResponse)) return false;
        ExecuteWebServiceUsingListResponse other = (ExecuteWebServiceUsingListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.executeWebServiceUsingListResult==null && other.getExecuteWebServiceUsingListResult()==null) || 
             (this.executeWebServiceUsingListResult!=null &&
              this.executeWebServiceUsingListResult.equals(other.getExecuteWebServiceUsingListResult())));
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
        if (getExecuteWebServiceUsingListResult() != null) {
            _hashCode += getExecuteWebServiceUsingListResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteWebServiceUsingListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteWebServiceUsingListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executeWebServiceUsingListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "ExecuteWebServiceUsingListResult"));
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
