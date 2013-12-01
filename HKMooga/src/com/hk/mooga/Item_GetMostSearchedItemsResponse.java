/**
 * Item_GetMostSearchedItemsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetMostSearchedItemsResponse  implements java.io.Serializable {
    private String item_GetMostSearchedItemsResult;

    public Item_GetMostSearchedItemsResponse() {
    }

    public Item_GetMostSearchedItemsResponse(
           String item_GetMostSearchedItemsResult) {
           this.item_GetMostSearchedItemsResult = item_GetMostSearchedItemsResult;
    }


    /**
     * Gets the item_GetMostSearchedItemsResult value for this Item_GetMostSearchedItemsResponse.
     * 
     * @return item_GetMostSearchedItemsResult
     */
    public String getItem_GetMostSearchedItemsResult() {
        return item_GetMostSearchedItemsResult;
    }


    /**
     * Sets the item_GetMostSearchedItemsResult value for this Item_GetMostSearchedItemsResponse.
     * 
     * @param item_GetMostSearchedItemsResult
     */
    public void setItem_GetMostSearchedItemsResult(String item_GetMostSearchedItemsResult) {
        this.item_GetMostSearchedItemsResult = item_GetMostSearchedItemsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetMostSearchedItemsResponse)) return false;
        Item_GetMostSearchedItemsResponse other = (Item_GetMostSearchedItemsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetMostSearchedItemsResult==null && other.getItem_GetMostSearchedItemsResult()==null) || 
             (this.item_GetMostSearchedItemsResult!=null &&
              this.item_GetMostSearchedItemsResult.equals(other.getItem_GetMostSearchedItemsResult())));
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
        if (getItem_GetMostSearchedItemsResult() != null) {
            _hashCode += getItem_GetMostSearchedItemsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetMostSearchedItemsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetMostSearchedItemsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetMostSearchedItemsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetMostSearchedItemsResult"));
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
