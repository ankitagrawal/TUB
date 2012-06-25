/**
 * Notify_NotifyData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.pact.service.mooga;

public class Notify_NotifyData  implements java.io.Serializable {
    private int p_Key;

    private java.lang.String p_UDFName;

    private java.lang.String p_Arguments;

    public Notify_NotifyData() {
    }

    public Notify_NotifyData(
           int p_Key,
           java.lang.String p_UDFName,
           java.lang.String p_Arguments) {
           this.p_Key = p_Key;
           this.p_UDFName = p_UDFName;
           this.p_Arguments = p_Arguments;
    }


    /**
     * Gets the p_Key value for this Notify_NotifyData.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Notify_NotifyData.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_UDFName value for this Notify_NotifyData.
     * 
     * @return p_UDFName
     */
    public java.lang.String getP_UDFName() {
        return p_UDFName;
    }


    /**
     * Sets the p_UDFName value for this Notify_NotifyData.
     * 
     * @param p_UDFName
     */
    public void setP_UDFName(java.lang.String p_UDFName) {
        this.p_UDFName = p_UDFName;
    }


    /**
     * Gets the p_Arguments value for this Notify_NotifyData.
     * 
     * @return p_Arguments
     */
    public java.lang.String getP_Arguments() {
        return p_Arguments;
    }


    /**
     * Sets the p_Arguments value for this Notify_NotifyData.
     * 
     * @param p_Arguments
     */
    public void setP_Arguments(java.lang.String p_Arguments) {
        this.p_Arguments = p_Arguments;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Notify_NotifyData)) return false;
        Notify_NotifyData other = (Notify_NotifyData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_UDFName==null && other.getP_UDFName()==null) || 
             (this.p_UDFName!=null &&
              this.p_UDFName.equals(other.getP_UDFName()))) &&
            ((this.p_Arguments==null && other.getP_Arguments()==null) || 
             (this.p_Arguments!=null &&
              this.p_Arguments.equals(other.getP_Arguments())));
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
        _hashCode += getP_Key();
        if (getP_UDFName() != null) {
            _hashCode += getP_UDFName().hashCode();
        }
        if (getP_Arguments() != null) {
            _hashCode += getP_Arguments().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Notify_NotifyData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Notify_NotifyData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_UDFName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_UDFName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Arguments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Arguments"));
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
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
