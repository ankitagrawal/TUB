/**
 * Item_GetRelatedItemsToItemResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetRelatedItemsToItemResponse  implements java.io.Serializable {
    private String item_GetRelatedItemsToItemResult;

    public Item_GetRelatedItemsToItemResponse() {
    }

    public Item_GetRelatedItemsToItemResponse(
           String item_GetRelatedItemsToItemResult) {
           this.item_GetRelatedItemsToItemResult = item_GetRelatedItemsToItemResult;
    }


    /**
     * Gets the item_GetRelatedItemsToItemResult value for this Item_GetRelatedItemsToItemResponse.
     * 
     * @return item_GetRelatedItemsToItemResult
     */
    public String getItem_GetRelatedItemsToItemResult() {
        return item_GetRelatedItemsToItemResult;
    }


    /**
     * Sets the item_GetRelatedItemsToItemResult value for this Item_GetRelatedItemsToItemResponse.
     * 
     * @param item_GetRelatedItemsToItemResult
     */
    public void setItem_GetRelatedItemsToItemResult(String item_GetRelatedItemsToItemResult) {
        this.item_GetRelatedItemsToItemResult = item_GetRelatedItemsToItemResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetRelatedItemsToItemResponse)) return false;
        Item_GetRelatedItemsToItemResponse other = (Item_GetRelatedItemsToItemResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetRelatedItemsToItemResult==null && other.getItem_GetRelatedItemsToItemResult()==null) || 
             (this.item_GetRelatedItemsToItemResult!=null &&
              this.item_GetRelatedItemsToItemResult.equals(other.getItem_GetRelatedItemsToItemResult())));
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
        if (getItem_GetRelatedItemsToItemResult() != null) {
            _hashCode += getItem_GetRelatedItemsToItemResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetRelatedItemsToItemResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetRelatedItemsToItemResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetRelatedItemsToItemResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetRelatedItemsToItemResult"));
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
