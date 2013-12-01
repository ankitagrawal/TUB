/**
 * Generic_GetPopularResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetPopularResponse  implements java.io.Serializable {
    private String generic_GetPopularResult;

    public Generic_GetPopularResponse() {
    }

    public Generic_GetPopularResponse(
           String generic_GetPopularResult) {
           this.generic_GetPopularResult = generic_GetPopularResult;
    }


    /**
     * Gets the generic_GetPopularResult value for this Generic_GetPopularResponse.
     * 
     * @return generic_GetPopularResult
     */
    public String getGeneric_GetPopularResult() {
        return generic_GetPopularResult;
    }


    /**
     * Sets the generic_GetPopularResult value for this Generic_GetPopularResponse.
     * 
     * @param generic_GetPopularResult
     */
    public void setGeneric_GetPopularResult(String generic_GetPopularResult) {
        this.generic_GetPopularResult = generic_GetPopularResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetPopularResponse)) return false;
        Generic_GetPopularResponse other = (Generic_GetPopularResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generic_GetPopularResult==null && other.getGeneric_GetPopularResult()==null) || 
             (this.generic_GetPopularResult!=null &&
              this.generic_GetPopularResult.equals(other.getGeneric_GetPopularResult())));
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
        if (getGeneric_GetPopularResult() != null) {
            _hashCode += getGeneric_GetPopularResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetPopularResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetPopularResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generic_GetPopularResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Generic_GetPopularResult"));
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
