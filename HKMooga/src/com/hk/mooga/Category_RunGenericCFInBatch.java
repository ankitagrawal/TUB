/**
 * Category_RunGenericCFInBatch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Category_RunGenericCFInBatch  implements java.io.Serializable {
    private int p_Key;

    private String p_ServiceCode;

    private String p_ServiceName;

    private String p_DBCriteria;

    public Category_RunGenericCFInBatch() {
    }

    public Category_RunGenericCFInBatch(
           int p_Key,
           String p_ServiceCode,
           String p_ServiceName,
           String p_DBCriteria) {
           this.p_Key = p_Key;
           this.p_ServiceCode = p_ServiceCode;
           this.p_ServiceName = p_ServiceName;
           this.p_DBCriteria = p_DBCriteria;
    }


    /**
     * Gets the p_Key value for this Category_RunGenericCFInBatch.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Category_RunGenericCFInBatch.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ServiceCode value for this Category_RunGenericCFInBatch.
     * 
     * @return p_ServiceCode
     */
    public String getP_ServiceCode() {
        return p_ServiceCode;
    }


    /**
     * Sets the p_ServiceCode value for this Category_RunGenericCFInBatch.
     * 
     * @param p_ServiceCode
     */
    public void setP_ServiceCode(String p_ServiceCode) {
        this.p_ServiceCode = p_ServiceCode;
    }


    /**
     * Gets the p_ServiceName value for this Category_RunGenericCFInBatch.
     * 
     * @return p_ServiceName
     */
    public String getP_ServiceName() {
        return p_ServiceName;
    }


    /**
     * Sets the p_ServiceName value for this Category_RunGenericCFInBatch.
     * 
     * @param p_ServiceName
     */
    public void setP_ServiceName(String p_ServiceName) {
        this.p_ServiceName = p_ServiceName;
    }


    /**
     * Gets the p_DBCriteria value for this Category_RunGenericCFInBatch.
     * 
     * @return p_DBCriteria
     */
    public String getP_DBCriteria() {
        return p_DBCriteria;
    }


    /**
     * Sets the p_DBCriteria value for this Category_RunGenericCFInBatch.
     * 
     * @param p_DBCriteria
     */
    public void setP_DBCriteria(String p_DBCriteria) {
        this.p_DBCriteria = p_DBCriteria;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Category_RunGenericCFInBatch)) return false;
        Category_RunGenericCFInBatch other = (Category_RunGenericCFInBatch) obj;
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
            ((this.p_ServiceName==null && other.getP_ServiceName()==null) || 
             (this.p_ServiceName!=null &&
              this.p_ServiceName.equals(other.getP_ServiceName()))) &&
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
        if (getP_ServiceName() != null) {
            _hashCode += getP_ServiceName().hashCode();
        }
        if (getP_DBCriteria() != null) {
            _hashCode += getP_DBCriteria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Category_RunGenericCFInBatch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Category_RunGenericCFInBatch"));
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
        elemField.setFieldName("p_ServiceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceName"));
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
