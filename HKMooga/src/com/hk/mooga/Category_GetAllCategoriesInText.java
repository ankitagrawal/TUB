/**
 * Category_GetAllCategoriesInText.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Category_GetAllCategoriesInText  implements java.io.Serializable {
    private int p_Key;

    private boolean p_IsPrefixWithHID;

    private String p_CategorySeparator;

    public Category_GetAllCategoriesInText() {
    }

    public Category_GetAllCategoriesInText(
           int p_Key,
           boolean p_IsPrefixWithHID,
           String p_CategorySeparator) {
           this.p_Key = p_Key;
           this.p_IsPrefixWithHID = p_IsPrefixWithHID;
           this.p_CategorySeparator = p_CategorySeparator;
    }


    /**
     * Gets the p_Key value for this Category_GetAllCategoriesInText.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Category_GetAllCategoriesInText.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_IsPrefixWithHID value for this Category_GetAllCategoriesInText.
     * 
     * @return p_IsPrefixWithHID
     */
    public boolean isP_IsPrefixWithHID() {
        return p_IsPrefixWithHID;
    }


    /**
     * Sets the p_IsPrefixWithHID value for this Category_GetAllCategoriesInText.
     * 
     * @param p_IsPrefixWithHID
     */
    public void setP_IsPrefixWithHID(boolean p_IsPrefixWithHID) {
        this.p_IsPrefixWithHID = p_IsPrefixWithHID;
    }


    /**
     * Gets the p_CategorySeparator value for this Category_GetAllCategoriesInText.
     * 
     * @return p_CategorySeparator
     */
    public String getP_CategorySeparator() {
        return p_CategorySeparator;
    }


    /**
     * Sets the p_CategorySeparator value for this Category_GetAllCategoriesInText.
     * 
     * @param p_CategorySeparator
     */
    public void setP_CategorySeparator(String p_CategorySeparator) {
        this.p_CategorySeparator = p_CategorySeparator;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Category_GetAllCategoriesInText)) return false;
        Category_GetAllCategoriesInText other = (Category_GetAllCategoriesInText) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            this.p_IsPrefixWithHID == other.isP_IsPrefixWithHID() &&
            ((this.p_CategorySeparator==null && other.getP_CategorySeparator()==null) || 
             (this.p_CategorySeparator!=null &&
              this.p_CategorySeparator.equals(other.getP_CategorySeparator())));
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
        _hashCode += (isP_IsPrefixWithHID() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getP_CategorySeparator() != null) {
            _hashCode += getP_CategorySeparator().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Category_GetAllCategoriesInText.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Category_GetAllCategoriesInText"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_IsPrefixWithHID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_IsPrefixWithHID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_CategorySeparator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_CategorySeparator"));
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
