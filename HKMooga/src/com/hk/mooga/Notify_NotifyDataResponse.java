/**
 * Notify_NotifyDataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Notify_NotifyDataResponse  implements java.io.Serializable {
    private String notify_NotifyDataResult;

    public Notify_NotifyDataResponse() {
    }

    public Notify_NotifyDataResponse(
           String notify_NotifyDataResult) {
           this.notify_NotifyDataResult = notify_NotifyDataResult;
    }


    /**
     * Gets the notify_NotifyDataResult value for this Notify_NotifyDataResponse.
     * 
     * @return notify_NotifyDataResult
     */
    public String getNotify_NotifyDataResult() {
        return notify_NotifyDataResult;
    }


    /**
     * Sets the notify_NotifyDataResult value for this Notify_NotifyDataResponse.
     * 
     * @param notify_NotifyDataResult
     */
    public void setNotify_NotifyDataResult(String notify_NotifyDataResult) {
        this.notify_NotifyDataResult = notify_NotifyDataResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Notify_NotifyDataResponse)) return false;
        Notify_NotifyDataResponse other = (Notify_NotifyDataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.notify_NotifyDataResult==null && other.getNotify_NotifyDataResult()==null) || 
             (this.notify_NotifyDataResult!=null &&
              this.notify_NotifyDataResult.equals(other.getNotify_NotifyDataResult())));
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
        if (getNotify_NotifyDataResult() != null) {
            _hashCode += getNotify_NotifyDataResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Notify_NotifyDataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Notify_NotifyDataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notify_NotifyDataResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Notify_NotifyDataResult"));
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
