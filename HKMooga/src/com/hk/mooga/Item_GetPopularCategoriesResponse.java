/**
 * Item_GetPopularCategoriesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetPopularCategoriesResponse  implements java.io.Serializable {
    private String item_GetPopularCategoriesResult;

    public Item_GetPopularCategoriesResponse() {
    }

    public Item_GetPopularCategoriesResponse(
           String item_GetPopularCategoriesResult) {
           this.item_GetPopularCategoriesResult = item_GetPopularCategoriesResult;
    }


    /**
     * Gets the item_GetPopularCategoriesResult value for this Item_GetPopularCategoriesResponse.
     * 
     * @return item_GetPopularCategoriesResult
     */
    public String getItem_GetPopularCategoriesResult() {
        return item_GetPopularCategoriesResult;
    }


    /**
     * Sets the item_GetPopularCategoriesResult value for this Item_GetPopularCategoriesResponse.
     * 
     * @param item_GetPopularCategoriesResult
     */
    public void setItem_GetPopularCategoriesResult(String item_GetPopularCategoriesResult) {
        this.item_GetPopularCategoriesResult = item_GetPopularCategoriesResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetPopularCategoriesResponse)) return false;
        Item_GetPopularCategoriesResponse other = (Item_GetPopularCategoriesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetPopularCategoriesResult==null && other.getItem_GetPopularCategoriesResult()==null) || 
             (this.item_GetPopularCategoriesResult!=null &&
              this.item_GetPopularCategoriesResult.equals(other.getItem_GetPopularCategoriesResult())));
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
        if (getItem_GetPopularCategoriesResult() != null) {
            _hashCode += getItem_GetPopularCategoriesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetPopularCategoriesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetPopularCategoriesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetPopularCategoriesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetPopularCategoriesResult"));
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
