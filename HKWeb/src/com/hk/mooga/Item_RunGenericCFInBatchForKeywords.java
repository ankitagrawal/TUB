/**
 * Item_RunGenericCFInBatchForKeywords.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_RunGenericCFInBatchForKeywords  implements java.io.Serializable {
    private int p_Key;

    private java.lang.String p_ServiceCode;

    private java.lang.String p_DBCriteria;

    public Item_RunGenericCFInBatchForKeywords() {
    }

    public Item_RunGenericCFInBatchForKeywords(
           int p_Key,
           java.lang.String p_ServiceCode,
           java.lang.String p_DBCriteria) {
           this.p_Key = p_Key;
           this.p_ServiceCode = p_ServiceCode;
           this.p_DBCriteria = p_DBCriteria;
    }


    /**
     * Gets the p_Key value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ServiceCode value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @return p_ServiceCode
     */
    public java.lang.String getP_ServiceCode() {
        return p_ServiceCode;
    }


    /**
     * Sets the p_ServiceCode value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @param p_ServiceCode
     */
    public void setP_ServiceCode(java.lang.String p_ServiceCode) {
        this.p_ServiceCode = p_ServiceCode;
    }


    /**
     * Gets the p_DBCriteria value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @return p_DBCriteria
     */
    public java.lang.String getP_DBCriteria() {
        return p_DBCriteria;
    }


    /**
     * Sets the p_DBCriteria value for this Item_RunGenericCFInBatchForKeywords.
     * 
     * @param p_DBCriteria
     */
    public void setP_DBCriteria(java.lang.String p_DBCriteria) {
        this.p_DBCriteria = p_DBCriteria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Item_RunGenericCFInBatchForKeywords)) return false;
        Item_RunGenericCFInBatchForKeywords other = (Item_RunGenericCFInBatchForKeywords) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ServiceCode==null && other.getP_ServiceCode()==null) || 
             (this.p_ServiceCode!=null &&
              this.p_ServiceCode.equals(other.getP_ServiceCode()))) &&
            ((this.p_DBCriteria==null && other.getP_DBCriteria()==null) || 
             (this.p_DBCriteria!=null &&
              this.p_DBCriteria.equals(other.getP_DBCriteria())));
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
        if (getP_ServiceCode() != null) {
            _hashCode += getP_ServiceCode().hashCode();
        }
        if (getP_DBCriteria() != null) {
            _hashCode += getP_DBCriteria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_RunGenericCFInBatchForKeywords.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_RunGenericCFInBatchForKeywords"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_DBCriteria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_DBCriteria"));
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
