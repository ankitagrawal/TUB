/**
 * ExecuteAllPendingServiceRequestResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteAllPendingServiceRequestResponse  implements java.io.Serializable {
    private String executeAllPendingServiceRequestResult;

    public ExecuteAllPendingServiceRequestResponse() {
    }

    public ExecuteAllPendingServiceRequestResponse(
           String executeAllPendingServiceRequestResult) {
           this.executeAllPendingServiceRequestResult = executeAllPendingServiceRequestResult;
    }


    /**
     * Gets the executeAllPendingServiceRequestResult value for this ExecuteAllPendingServiceRequestResponse.
     * 
     * @return executeAllPendingServiceRequestResult
     */
    public String getExecuteAllPendingServiceRequestResult() {
        return executeAllPendingServiceRequestResult;
    }


    /**
     * Sets the executeAllPendingServiceRequestResult value for this ExecuteAllPendingServiceRequestResponse.
     * 
     * @param executeAllPendingServiceRequestResult
     */
    public void setExecuteAllPendingServiceRequestResult(String executeAllPendingServiceRequestResult) {
        this.executeAllPendingServiceRequestResult = executeAllPendingServiceRequestResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteAllPendingServiceRequestResponse)) return false;
        ExecuteAllPendingServiceRequestResponse other = (ExecuteAllPendingServiceRequestResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.executeAllPendingServiceRequestResult==null && other.getExecuteAllPendingServiceRequestResult()==null) || 
             (this.executeAllPendingServiceRequestResult!=null &&
              this.executeAllPendingServiceRequestResult.equals(other.getExecuteAllPendingServiceRequestResult())));
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
        if (getExecuteAllPendingServiceRequestResult() != null) {
            _hashCode += getExecuteAllPendingServiceRequestResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteAllPendingServiceRequestResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteAllPendingServiceRequestResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executeAllPendingServiceRequestResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "ExecuteAllPendingServiceRequestResult"));
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
