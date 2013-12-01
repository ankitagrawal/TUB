/**
 * Notify_NotifyDataUsingListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Notify_NotifyDataUsingListResponse  implements java.io.Serializable {
    private String notify_NotifyDataUsingListResult;

    public Notify_NotifyDataUsingListResponse() {
    }

    public Notify_NotifyDataUsingListResponse(
           String notify_NotifyDataUsingListResult) {
           this.notify_NotifyDataUsingListResult = notify_NotifyDataUsingListResult;
    }


    /**
     * Gets the notify_NotifyDataUsingListResult value for this Notify_NotifyDataUsingListResponse.
     * 
     * @return notify_NotifyDataUsingListResult
     */
    public String getNotify_NotifyDataUsingListResult() {
        return notify_NotifyDataUsingListResult;
    }


    /**
     * Sets the notify_NotifyDataUsingListResult value for this Notify_NotifyDataUsingListResponse.
     * 
     * @param notify_NotifyDataUsingListResult
     */
    public void setNotify_NotifyDataUsingListResult(String notify_NotifyDataUsingListResult) {
        this.notify_NotifyDataUsingListResult = notify_NotifyDataUsingListResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Notify_NotifyDataUsingListResponse)) return false;
        Notify_NotifyDataUsingListResponse other = (Notify_NotifyDataUsingListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.notify_NotifyDataUsingListResult==null && other.getNotify_NotifyDataUsingListResult()==null) || 
             (this.notify_NotifyDataUsingListResult!=null &&
              this.notify_NotifyDataUsingListResult.equals(other.getNotify_NotifyDataUsingListResult())));
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
        if (getNotify_NotifyDataUsingListResult() != null) {
            _hashCode += getNotify_NotifyDataUsingListResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Notify_NotifyDataUsingListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Notify_NotifyDataUsingListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notify_NotifyDataUsingListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Notify_NotifyDataUsingListResult"));
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
