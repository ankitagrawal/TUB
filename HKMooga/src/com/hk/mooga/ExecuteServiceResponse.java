/**
 * ExecuteServiceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteServiceResponse  implements java.io.Serializable {
    private String executeServiceResult;

    public ExecuteServiceResponse() {
    }

    public ExecuteServiceResponse(
           String executeServiceResult) {
           this.executeServiceResult = executeServiceResult;
    }


    /**
     * Gets the executeServiceResult value for this ExecuteServiceResponse.
     * 
     * @return executeServiceResult
     */
    public String getExecuteServiceResult() {
        return executeServiceResult;
    }


    /**
     * Sets the executeServiceResult value for this ExecuteServiceResponse.
     * 
     * @param executeServiceResult
     */
    public void setExecuteServiceResult(String executeServiceResult) {
        this.executeServiceResult = executeServiceResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteServiceResponse)) return false;
        ExecuteServiceResponse other = (ExecuteServiceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.executeServiceResult==null && other.getExecuteServiceResult()==null) || 
             (this.executeServiceResult!=null &&
              this.executeServiceResult.equals(other.getExecuteServiceResult())));
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
        if (getExecuteServiceResult() != null) {
            _hashCode += getExecuteServiceResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteServiceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executeServiceResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "ExecuteServiceResult"));
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
