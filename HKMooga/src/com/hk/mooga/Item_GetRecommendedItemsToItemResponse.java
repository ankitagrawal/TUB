/**
 * Item_GetRecommendedItemsToItemResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetRecommendedItemsToItemResponse  implements java.io.Serializable {
    private String item_GetRecommendedItemsToItemResult;

    public Item_GetRecommendedItemsToItemResponse() {
    }

    public Item_GetRecommendedItemsToItemResponse(
           String item_GetRecommendedItemsToItemResult) {
           this.item_GetRecommendedItemsToItemResult = item_GetRecommendedItemsToItemResult;
    }


    /**
     * Gets the item_GetRecommendedItemsToItemResult value for this Item_GetRecommendedItemsToItemResponse.
     * 
     * @return item_GetRecommendedItemsToItemResult
     */
    public String getItem_GetRecommendedItemsToItemResult() {
        return item_GetRecommendedItemsToItemResult;
    }


    /**
     * Sets the item_GetRecommendedItemsToItemResult value for this Item_GetRecommendedItemsToItemResponse.
     * 
     * @param item_GetRecommendedItemsToItemResult
     */
    public void setItem_GetRecommendedItemsToItemResult(String item_GetRecommendedItemsToItemResult) {
        this.item_GetRecommendedItemsToItemResult = item_GetRecommendedItemsToItemResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetRecommendedItemsToItemResponse)) return false;
        Item_GetRecommendedItemsToItemResponse other = (Item_GetRecommendedItemsToItemResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetRecommendedItemsToItemResult==null && other.getItem_GetRecommendedItemsToItemResult()==null) || 
             (this.item_GetRecommendedItemsToItemResult!=null &&
              this.item_GetRecommendedItemsToItemResult.equals(other.getItem_GetRecommendedItemsToItemResult())));
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
        if (getItem_GetRecommendedItemsToItemResult() != null) {
            _hashCode += getItem_GetRecommendedItemsToItemResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetRecommendedItemsToItemResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetRecommendedItemsToItemResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetRecommendedItemsToItemResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetRecommendedItemsToItemResult"));
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
