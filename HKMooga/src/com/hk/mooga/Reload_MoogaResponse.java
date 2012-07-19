/**
 * Reload_MoogaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Reload_MoogaResponse  implements java.io.Serializable {
    private String reload_MoogaResult;

    public Reload_MoogaResponse() {
    }

    public Reload_MoogaResponse(
           String reload_MoogaResult) {
           this.reload_MoogaResult = reload_MoogaResult;
    }


    /**
     * Gets the reload_MoogaResult value for this Reload_MoogaResponse.
     * 
     * @return reload_MoogaResult
     */
    public String getReload_MoogaResult() {
        return reload_MoogaResult;
    }


    /**
     * Sets the reload_MoogaResult value for this Reload_MoogaResponse.
     * 
     * @param reload_MoogaResult
     */
    public void setReload_MoogaResult(String reload_MoogaResult) {
        this.reload_MoogaResult = reload_MoogaResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Reload_MoogaResponse)) return false;
        Reload_MoogaResponse other = (Reload_MoogaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.reload_MoogaResult==null && other.getReload_MoogaResult()==null) || 
             (this.reload_MoogaResult!=null &&
              this.reload_MoogaResult.equals(other.getReload_MoogaResult())));
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
        if (getReload_MoogaResult() != null) {
            _hashCode += getReload_MoogaResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Reload_MoogaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Reload_MoogaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reload_MoogaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Reload_MoogaResult"));
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
