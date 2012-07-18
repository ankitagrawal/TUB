/**
 * Item_GetPopularItemsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetPopularItemsResponse  implements java.io.Serializable {
    private String item_GetPopularItemsResult;

    public Item_GetPopularItemsResponse() {
    }

    public Item_GetPopularItemsResponse(
           String item_GetPopularItemsResult) {
           this.item_GetPopularItemsResult = item_GetPopularItemsResult;
    }


    /**
     * Gets the item_GetPopularItemsResult value for this Item_GetPopularItemsResponse.
     * 
     * @return item_GetPopularItemsResult
     */
    public String getItem_GetPopularItemsResult() {
        return item_GetPopularItemsResult;
    }


    /**
     * Sets the item_GetPopularItemsResult value for this Item_GetPopularItemsResponse.
     * 
     * @param item_GetPopularItemsResult
     */
    public void setItem_GetPopularItemsResult(String item_GetPopularItemsResult) {
        this.item_GetPopularItemsResult = item_GetPopularItemsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetPopularItemsResponse)) return false;
        Item_GetPopularItemsResponse other = (Item_GetPopularItemsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetPopularItemsResult==null && other.getItem_GetPopularItemsResult()==null) || 
             (this.item_GetPopularItemsResult!=null &&
              this.item_GetPopularItemsResult.equals(other.getItem_GetPopularItemsResult())));
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
        if (getItem_GetPopularItemsResult() != null) {
            _hashCode += getItem_GetPopularItemsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetPopularItemsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetPopularItemsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetPopularItemsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetPopularItemsResult"));
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
