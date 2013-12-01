/**
 * Item_GetRecommendedKeywordsToKeyword.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetRecommendedKeywordsToKeyword  implements java.io.Serializable {
    private int p_Key;

    private String p_Keyword;

    private String p_ExtItemCategoryID;

    private String p_ExtLocationID;

    private String p_ServiceCode;

    public Item_GetRecommendedKeywordsToKeyword() {
    }

    public Item_GetRecommendedKeywordsToKeyword(
           int p_Key,
           String p_Keyword,
           String p_ExtItemCategoryID,
           String p_ExtLocationID,
           String p_ServiceCode) {
           this.p_Key = p_Key;
           this.p_Keyword = p_Keyword;
           this.p_ExtItemCategoryID = p_ExtItemCategoryID;
           this.p_ExtLocationID = p_ExtLocationID;
           this.p_ServiceCode = p_ServiceCode;
    }


    /**
     * Gets the p_Key value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_Keyword value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @return p_Keyword
     */
    public String getP_Keyword() {
        return p_Keyword;
    }


    /**
     * Sets the p_Keyword value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @param p_Keyword
     */
    public void setP_Keyword(String p_Keyword) {
        this.p_Keyword = p_Keyword;
    }


    /**
     * Gets the p_ExtItemCategoryID value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @return p_ExtItemCategoryID
     */
    public String getP_ExtItemCategoryID() {
        return p_ExtItemCategoryID;
    }


    /**
     * Sets the p_ExtItemCategoryID value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @param p_ExtItemCategoryID
     */
    public void setP_ExtItemCategoryID(String p_ExtItemCategoryID) {
        this.p_ExtItemCategoryID = p_ExtItemCategoryID;
    }


    /**
     * Gets the p_ExtLocationID value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @return p_ExtLocationID
     */
    public String getP_ExtLocationID() {
        return p_ExtLocationID;
    }


    /**
     * Sets the p_ExtLocationID value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @param p_ExtLocationID
     */
    public void setP_ExtLocationID(String p_ExtLocationID) {
        this.p_ExtLocationID = p_ExtLocationID;
    }


    /**
     * Gets the p_ServiceCode value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @return p_ServiceCode
     */
    public String getP_ServiceCode() {
        return p_ServiceCode;
    }


    /**
     * Sets the p_ServiceCode value for this Item_GetRecommendedKeywordsToKeyword.
     * 
     * @param p_ServiceCode
     */
    public void setP_ServiceCode(String p_ServiceCode) {
        this.p_ServiceCode = p_ServiceCode;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetRecommendedKeywordsToKeyword)) return false;
        Item_GetRecommendedKeywordsToKeyword other = (Item_GetRecommendedKeywordsToKeyword) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_Keyword==null && other.getP_Keyword()==null) || 
             (this.p_Keyword!=null &&
              this.p_Keyword.equals(other.getP_Keyword()))) &&
            ((this.p_ExtItemCategoryID==null && other.getP_ExtItemCategoryID()==null) || 
             (this.p_ExtItemCategoryID!=null &&
              this.p_ExtItemCategoryID.equals(other.getP_ExtItemCategoryID()))) &&
            ((this.p_ExtLocationID==null && other.getP_ExtLocationID()==null) || 
             (this.p_ExtLocationID!=null &&
              this.p_ExtLocationID.equals(other.getP_ExtLocationID()))) &&
            ((this.p_ServiceCode==null && other.getP_ServiceCode()==null) || 
             (this.p_ServiceCode!=null &&
              this.p_ServiceCode.equals(other.getP_ServiceCode())));
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
        if (getP_Keyword() != null) {
            _hashCode += getP_Keyword().hashCode();
        }
        if (getP_ExtItemCategoryID() != null) {
            _hashCode += getP_ExtItemCategoryID().hashCode();
        }
        if (getP_ExtLocationID() != null) {
            _hashCode += getP_ExtLocationID().hashCode();
        }
        if (getP_ServiceCode() != null) {
            _hashCode += getP_ServiceCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetRecommendedKeywordsToKeyword.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetRecommendedKeywordsToKeyword"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Keyword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Keyword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("p_ExtLocationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtLocationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceCode"));
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
