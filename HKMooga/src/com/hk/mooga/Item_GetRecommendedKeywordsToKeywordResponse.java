/**
 * Item_GetRecommendedKeywordsToKeywordResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetRecommendedKeywordsToKeywordResponse  implements java.io.Serializable {
    private String item_GetRecommendedKeywordsToKeywordResult;

    public Item_GetRecommendedKeywordsToKeywordResponse() {
    }

    public Item_GetRecommendedKeywordsToKeywordResponse(
           String item_GetRecommendedKeywordsToKeywordResult) {
           this.item_GetRecommendedKeywordsToKeywordResult = item_GetRecommendedKeywordsToKeywordResult;
    }


    /**
     * Gets the item_GetRecommendedKeywordsToKeywordResult value for this Item_GetRecommendedKeywordsToKeywordResponse.
     * 
     * @return item_GetRecommendedKeywordsToKeywordResult
     */
    public String getItem_GetRecommendedKeywordsToKeywordResult() {
        return item_GetRecommendedKeywordsToKeywordResult;
    }


    /**
     * Sets the item_GetRecommendedKeywordsToKeywordResult value for this Item_GetRecommendedKeywordsToKeywordResponse.
     * 
     * @param item_GetRecommendedKeywordsToKeywordResult
     */
    public void setItem_GetRecommendedKeywordsToKeywordResult(String item_GetRecommendedKeywordsToKeywordResult) {
        this.item_GetRecommendedKeywordsToKeywordResult = item_GetRecommendedKeywordsToKeywordResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetRecommendedKeywordsToKeywordResponse)) return false;
        Item_GetRecommendedKeywordsToKeywordResponse other = (Item_GetRecommendedKeywordsToKeywordResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetRecommendedKeywordsToKeywordResult==null && other.getItem_GetRecommendedKeywordsToKeywordResult()==null) || 
             (this.item_GetRecommendedKeywordsToKeywordResult!=null &&
              this.item_GetRecommendedKeywordsToKeywordResult.equals(other.getItem_GetRecommendedKeywordsToKeywordResult())));
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
        if (getItem_GetRecommendedKeywordsToKeywordResult() != null) {
            _hashCode += getItem_GetRecommendedKeywordsToKeywordResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetRecommendedKeywordsToKeywordResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetRecommendedKeywordsToKeywordResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetRecommendedKeywordsToKeywordResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetRecommendedKeywordsToKeywordResult"));
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
