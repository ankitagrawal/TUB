/**
 * Item_GetRelatedKeywordsToKeywordResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetRelatedKeywordsToKeywordResponse  implements java.io.Serializable {
    private java.lang.String item_GetRelatedKeywordsToKeywordResult;

    public Item_GetRelatedKeywordsToKeywordResponse() {
    }

    public Item_GetRelatedKeywordsToKeywordResponse(
           java.lang.String item_GetRelatedKeywordsToKeywordResult) {
           this.item_GetRelatedKeywordsToKeywordResult = item_GetRelatedKeywordsToKeywordResult;
    }


    /**
     * Gets the item_GetRelatedKeywordsToKeywordResult value for this Item_GetRelatedKeywordsToKeywordResponse.
     * 
     * @return item_GetRelatedKeywordsToKeywordResult
     */
    public java.lang.String getItem_GetRelatedKeywordsToKeywordResult() {
        return item_GetRelatedKeywordsToKeywordResult;
    }


    /**
     * Sets the item_GetRelatedKeywordsToKeywordResult value for this Item_GetRelatedKeywordsToKeywordResponse.
     * 
     * @param item_GetRelatedKeywordsToKeywordResult
     */
    public void setItem_GetRelatedKeywordsToKeywordResult(java.lang.String item_GetRelatedKeywordsToKeywordResult) {
        this.item_GetRelatedKeywordsToKeywordResult = item_GetRelatedKeywordsToKeywordResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Item_GetRelatedKeywordsToKeywordResponse)) return false;
        Item_GetRelatedKeywordsToKeywordResponse other = (Item_GetRelatedKeywordsToKeywordResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetRelatedKeywordsToKeywordResult==null && other.getItem_GetRelatedKeywordsToKeywordResult()==null) || 
             (this.item_GetRelatedKeywordsToKeywordResult!=null &&
              this.item_GetRelatedKeywordsToKeywordResult.equals(other.getItem_GetRelatedKeywordsToKeywordResult())));
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
        if (getItem_GetRelatedKeywordsToKeywordResult() != null) {
            _hashCode += getItem_GetRelatedKeywordsToKeywordResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetRelatedKeywordsToKeywordResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetRelatedKeywordsToKeywordResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetRelatedKeywordsToKeywordResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetRelatedKeywordsToKeywordResult"));
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
