/**
 * GetApplicationLoadStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class GetApplicationLoadStatusResponse  implements java.io.Serializable {
    private String getApplicationLoadStatusResult;

    public GetApplicationLoadStatusResponse() {
    }

    public GetApplicationLoadStatusResponse(
           String getApplicationLoadStatusResult) {
           this.getApplicationLoadStatusResult = getApplicationLoadStatusResult;
    }


    /**
     * Gets the getApplicationLoadStatusResult value for this GetApplicationLoadStatusResponse.
     * 
     * @return getApplicationLoadStatusResult
     */
    public String getGetApplicationLoadStatusResult() {
        return getApplicationLoadStatusResult;
    }


    /**
     * Sets the getApplicationLoadStatusResult value for this GetApplicationLoadStatusResponse.
     * 
     * @param getApplicationLoadStatusResult
     */
    public void setGetApplicationLoadStatusResult(String getApplicationLoadStatusResult) {
        this.getApplicationLoadStatusResult = getApplicationLoadStatusResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetApplicationLoadStatusResponse)) return false;
        GetApplicationLoadStatusResponse other = (GetApplicationLoadStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getApplicationLoadStatusResult==null && other.getGetApplicationLoadStatusResult()==null) || 
             (this.getApplicationLoadStatusResult!=null &&
              this.getApplicationLoadStatusResult.equals(other.getGetApplicationLoadStatusResult())));
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
        if (getGetApplicationLoadStatusResult() != null) {
            _hashCode += getGetApplicationLoadStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetApplicationLoadStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">GetApplicationLoadStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getApplicationLoadStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "GetApplicationLoadStatusResult"));
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
