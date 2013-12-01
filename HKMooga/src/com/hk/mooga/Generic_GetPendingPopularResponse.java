/**
 * Generic_GetPendingPopularResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetPendingPopularResponse  implements java.io.Serializable {
    private String generic_GetPendingPopularResult;

    public Generic_GetPendingPopularResponse() {
    }

    public Generic_GetPendingPopularResponse(
           String generic_GetPendingPopularResult) {
           this.generic_GetPendingPopularResult = generic_GetPendingPopularResult;
    }


    /**
     * Gets the generic_GetPendingPopularResult value for this Generic_GetPendingPopularResponse.
     * 
     * @return generic_GetPendingPopularResult
     */
    public String getGeneric_GetPendingPopularResult() {
        return generic_GetPendingPopularResult;
    }


    /**
     * Sets the generic_GetPendingPopularResult value for this Generic_GetPendingPopularResponse.
     * 
     * @param generic_GetPendingPopularResult
     */
    public void setGeneric_GetPendingPopularResult(String generic_GetPendingPopularResult) {
        this.generic_GetPendingPopularResult = generic_GetPendingPopularResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetPendingPopularResponse)) return false;
        Generic_GetPendingPopularResponse other = (Generic_GetPendingPopularResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generic_GetPendingPopularResult==null && other.getGeneric_GetPendingPopularResult()==null) || 
             (this.generic_GetPendingPopularResult!=null &&
              this.generic_GetPendingPopularResult.equals(other.getGeneric_GetPendingPopularResult())));
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
        if (getGeneric_GetPendingPopularResult() != null) {
            _hashCode += getGeneric_GetPendingPopularResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetPendingPopularResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetPendingPopularResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generic_GetPendingPopularResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Generic_GetPendingPopularResult"));
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
