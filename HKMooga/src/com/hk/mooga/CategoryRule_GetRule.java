/**
 * CategoryRule_GetRule.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class CategoryRule_GetRule  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtItemCategoryID;

    private String p_ServiceName;

    private String p_FeatureName;

    public CategoryRule_GetRule() {
    }

    public CategoryRule_GetRule(
           int p_Key,
           String p_ExtItemCategoryID,
           String p_ServiceName,
           String p_FeatureName) {
           this.p_Key = p_Key;
           this.p_ExtItemCategoryID = p_ExtItemCategoryID;
           this.p_ServiceName = p_ServiceName;
           this.p_FeatureName = p_FeatureName;
    }


    /**
     * Gets the p_Key value for this CategoryRule_GetRule.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this CategoryRule_GetRule.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtItemCategoryID value for this CategoryRule_GetRule.
     * 
     * @return p_ExtItemCategoryID
     */
    public String getP_ExtItemCategoryID() {
        return p_ExtItemCategoryID;
    }


    /**
     * Sets the p_ExtItemCategoryID value for this CategoryRule_GetRule.
     * 
     * @param p_ExtItemCategoryID
     */
    public void setP_ExtItemCategoryID(String p_ExtItemCategoryID) {
        this.p_ExtItemCategoryID = p_ExtItemCategoryID;
    }


    /**
     * Gets the p_ServiceName value for this CategoryRule_GetRule.
     * 
     * @return p_ServiceName
     */
    public String getP_ServiceName() {
        return p_ServiceName;
    }


    /**
     * Sets the p_ServiceName value for this CategoryRule_GetRule.
     * 
     * @param p_ServiceName
     */
    public void setP_ServiceName(String p_ServiceName) {
        this.p_ServiceName = p_ServiceName;
    }


    /**
     * Gets the p_FeatureName value for this CategoryRule_GetRule.
     * 
     * @return p_FeatureName
     */
    public String getP_FeatureName() {
        return p_FeatureName;
    }


    /**
     * Sets the p_FeatureName value for this CategoryRule_GetRule.
     * 
     * @param p_FeatureName
     */
    public void setP_FeatureName(String p_FeatureName) {
        this.p_FeatureName = p_FeatureName;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof CategoryRule_GetRule)) return false;
        CategoryRule_GetRule other = (CategoryRule_GetRule) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtItemCategoryID==null && other.getP_ExtItemCategoryID()==null) || 
             (this.p_ExtItemCategoryID!=null &&
              this.p_ExtItemCategoryID.equals(other.getP_ExtItemCategoryID()))) &&
            ((this.p_ServiceName==null && other.getP_ServiceName()==null) || 
             (this.p_ServiceName!=null &&
              this.p_ServiceName.equals(other.getP_ServiceName()))) &&
            ((this.p_FeatureName==null && other.getP_FeatureName()==null) || 
             (this.p_FeatureName!=null &&
              this.p_FeatureName.equals(other.getP_FeatureName())));
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
        if (getP_ExtItemCategoryID() != null) {
            _hashCode += getP_ExtItemCategoryID().hashCode();
        }
        if (getP_ServiceName() != null) {
            _hashCode += getP_ServiceName().hashCode();
        }
        if (getP_FeatureName() != null) {
            _hashCode += getP_FeatureName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CategoryRule_GetRule.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">CategoryRule_GetRule"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtItemCategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtItemCategoryID"));
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
        elemField.setFieldName("p_FeatureName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_FeatureName"));
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
