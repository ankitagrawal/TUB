/**
 * Item_GetMostAccessedItemsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetMostAccessedItemsResponse  implements java.io.Serializable {
    private String item_GetMostAccessedItemsResult;

    public Item_GetMostAccessedItemsResponse() {
    }

    public Item_GetMostAccessedItemsResponse(
           String item_GetMostAccessedItemsResult) {
           this.item_GetMostAccessedItemsResult = item_GetMostAccessedItemsResult;
    }


    /**
     * Gets the item_GetMostAccessedItemsResult value for this Item_GetMostAccessedItemsResponse.
     * 
     * @return item_GetMostAccessedItemsResult
     */
    public String getItem_GetMostAccessedItemsResult() {
        return item_GetMostAccessedItemsResult;
    }


    /**
     * Sets the item_GetMostAccessedItemsResult value for this Item_GetMostAccessedItemsResponse.
     * 
     * @param item_GetMostAccessedItemsResult
     */
    public void setItem_GetMostAccessedItemsResult(String item_GetMostAccessedItemsResult) {
        this.item_GetMostAccessedItemsResult = item_GetMostAccessedItemsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetMostAccessedItemsResponse)) return false;
        Item_GetMostAccessedItemsResponse other = (Item_GetMostAccessedItemsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetMostAccessedItemsResult==null && other.getItem_GetMostAccessedItemsResult()==null) || 
             (this.item_GetMostAccessedItemsResult!=null &&
              this.item_GetMostAccessedItemsResult.equals(other.getItem_GetMostAccessedItemsResult())));
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
        if (getItem_GetMostAccessedItemsResult() != null) {
            _hashCode += getItem_GetMostAccessedItemsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetMostAccessedItemsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetMostAccessedItemsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetMostAccessedItemsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetMostAccessedItemsResult"));
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
