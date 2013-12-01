/**
 * Generic_GenericPushResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GenericPushResponse  implements java.io.Serializable {
    private String generic_GenericPushResult;

    public Generic_GenericPushResponse() {
    }

    public Generic_GenericPushResponse(
           String generic_GenericPushResult) {
           this.generic_GenericPushResult = generic_GenericPushResult;
    }


    /**
     * Gets the generic_GenericPushResult value for this Generic_GenericPushResponse.
     * 
     * @return generic_GenericPushResult
     */
    public String getGeneric_GenericPushResult() {
        return generic_GenericPushResult;
    }


    /**
     * Sets the generic_GenericPushResult value for this Generic_GenericPushResponse.
     * 
     * @param generic_GenericPushResult
     */
    public void setGeneric_GenericPushResult(String generic_GenericPushResult) {
        this.generic_GenericPushResult = generic_GenericPushResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GenericPushResponse)) return false;
        Generic_GenericPushResponse other = (Generic_GenericPushResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.generic_GenericPushResult==null && other.getGeneric_GenericPushResult()==null) || 
             (this.generic_GenericPushResult!=null &&
              this.generic_GenericPushResult.equals(other.getGeneric_GenericPushResult())));
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
        if (getGeneric_GenericPushResult() != null) {
            _hashCode += getGeneric_GenericPushResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GenericPushResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GenericPushResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generic_GenericPushResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Generic_GenericPushResult"));
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
