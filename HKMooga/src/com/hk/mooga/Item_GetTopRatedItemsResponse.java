/**
 * Item_GetTopRatedItemsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetTopRatedItemsResponse  implements java.io.Serializable {
    private String item_GetTopRatedItemsResult;

    public Item_GetTopRatedItemsResponse() {
    }

    public Item_GetTopRatedItemsResponse(
           String item_GetTopRatedItemsResult) {
           this.item_GetTopRatedItemsResult = item_GetTopRatedItemsResult;
    }


    /**
     * Gets the item_GetTopRatedItemsResult value for this Item_GetTopRatedItemsResponse.
     * 
     * @return item_GetTopRatedItemsResult
     */
    public String getItem_GetTopRatedItemsResult() {
        return item_GetTopRatedItemsResult;
    }


    /**
     * Sets the item_GetTopRatedItemsResult value for this Item_GetTopRatedItemsResponse.
     * 
     * @param item_GetTopRatedItemsResult
     */
    public void setItem_GetTopRatedItemsResult(String item_GetTopRatedItemsResult) {
        this.item_GetTopRatedItemsResult = item_GetTopRatedItemsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetTopRatedItemsResponse)) return false;
        Item_GetTopRatedItemsResponse other = (Item_GetTopRatedItemsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetTopRatedItemsResult==null && other.getItem_GetTopRatedItemsResult()==null) || 
             (this.item_GetTopRatedItemsResult!=null &&
              this.item_GetTopRatedItemsResult.equals(other.getItem_GetTopRatedItemsResult())));
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
        if (getItem_GetTopRatedItemsResult() != null) {
            _hashCode += getItem_GetTopRatedItemsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetTopRatedItemsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetTopRatedItemsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetTopRatedItemsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetTopRatedItemsResult"));
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
