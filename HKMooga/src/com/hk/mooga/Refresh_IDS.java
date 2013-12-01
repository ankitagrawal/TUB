/**
 * Refresh_IDS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Refresh_IDS  implements java.io.Serializable {
    private int p_Key;

    private String p_Application;

    private String p_MoogaIDType;

    public Refresh_IDS() {
    }

    public Refresh_IDS(
           int p_Key,
           String p_Application,
           String p_MoogaIDType) {
           this.p_Key = p_Key;
           this.p_Application = p_Application;
           this.p_MoogaIDType = p_MoogaIDType;
    }


    /**
     * Gets the p_Key value for this Refresh_IDS.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Refresh_IDS.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_Application value for this Refresh_IDS.
     * 
     * @return p_Application
     */
    public String getP_Application() {
        return p_Application;
    }


    /**
     * Sets the p_Application value for this Refresh_IDS.
     * 
     * @param p_Application
     */
    public void setP_Application(String p_Application) {
        this.p_Application = p_Application;
    }


    /**
     * Gets the p_MoogaIDType value for this Refresh_IDS.
     * 
     * @return p_MoogaIDType
     */
    public String getP_MoogaIDType() {
        return p_MoogaIDType;
    }


    /**
     * Sets the p_MoogaIDType value for this Refresh_IDS.
     * 
     * @param p_MoogaIDType
     */
    public void setP_MoogaIDType(String p_MoogaIDType) {
        this.p_MoogaIDType = p_MoogaIDType;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Refresh_IDS)) return false;
        Refresh_IDS other = (Refresh_IDS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_Application==null && other.getP_Application()==null) || 
             (this.p_Application!=null &&
              this.p_Application.equals(other.getP_Application()))) &&
            ((this.p_MoogaIDType==null && other.getP_MoogaIDType()==null) || 
             (this.p_MoogaIDType!=null &&
              this.p_MoogaIDType.equals(other.getP_MoogaIDType())));
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
        if (getP_Application() != null) {
            _hashCode += getP_Application().hashCode();
        }
        if (getP_MoogaIDType() != null) {
            _hashCode += getP_MoogaIDType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Refresh_IDS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Refresh_IDS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Application");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Application"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_MoogaIDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_MoogaIDType"));
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
