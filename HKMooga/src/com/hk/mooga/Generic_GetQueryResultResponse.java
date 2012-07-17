/**
 * Generic_GetQueryResultResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetQueryResultResponse  implements java.io.Serializable {
    private String generic_GetQueryResultResult;

    public Generic_GetQueryResultResponse() {
    }

    public Generic_GetQueryResultResponse(
           String generic_GetQueryResultResult) {
           this.generic_GetQueryResultResult = generic_GetQueryResultResult;
    }


    /**
     * Gets the generic_GetQueryResultResult value for this Generic_GetQueryResultResponse.
     * 
     * @return generic_GetQueryResultResult
     */
    public String getGeneric_GetQueryResultResult() {
        return generic_GetQueryResultResult;
    }


    /**
     * Sets the generic_GetQueryResultResult value for this Generic_GetQueryResultResponse.
     * 
     * @param generic_GetQueryResultResult
     */
    public void setGeneric_GetQueryResultResult(String generic_GetQueryResultResult) {
        this.generic_GetQueryResultResult = generic_GetQueryResultResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetQueryResultResponse)) return false;
        Generic_GetQueryResultResponse other = (Generic_GetQueryResultResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generic_GetQueryResultResult==null && other.getGeneric_GetQueryResultResult()==null) || 
             (this.generic_GetQueryResultResult!=null &&
              this.generic_GetQueryResultResult.equals(other.getGeneric_GetQueryResultResult())));
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
        if (getGeneric_GetQueryResultResult() != null) {
            _hashCode += getGeneric_GetQueryResultResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetQueryResultResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetQueryResultResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generic_GetQueryResultResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Generic_GetQueryResultResult"));
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
