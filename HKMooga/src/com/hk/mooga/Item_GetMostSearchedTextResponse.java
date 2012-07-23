/**
 * Item_GetMostSearchedTextResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetMostSearchedTextResponse  implements java.io.Serializable {
    private String item_GetMostSearchedTextResult;

    public Item_GetMostSearchedTextResponse() {
    }

    public Item_GetMostSearchedTextResponse(
           String item_GetMostSearchedTextResult) {
           this.item_GetMostSearchedTextResult = item_GetMostSearchedTextResult;
    }


    /**
     * Gets the item_GetMostSearchedTextResult value for this Item_GetMostSearchedTextResponse.
     * 
     * @return item_GetMostSearchedTextResult
     */
    public String getItem_GetMostSearchedTextResult() {
        return item_GetMostSearchedTextResult;
    }


    /**
     * Sets the item_GetMostSearchedTextResult value for this Item_GetMostSearchedTextResponse.
     * 
     * @param item_GetMostSearchedTextResult
     */
    public void setItem_GetMostSearchedTextResult(String item_GetMostSearchedTextResult) {
        this.item_GetMostSearchedTextResult = item_GetMostSearchedTextResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetMostSearchedTextResponse)) return false;
        Item_GetMostSearchedTextResponse other = (Item_GetMostSearchedTextResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetMostSearchedTextResult==null && other.getItem_GetMostSearchedTextResult()==null) || 
             (this.item_GetMostSearchedTextResult!=null &&
              this.item_GetMostSearchedTextResult.equals(other.getItem_GetMostSearchedTextResult())));
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
        if (getItem_GetMostSearchedTextResult() != null) {
            _hashCode += getItem_GetMostSearchedTextResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetMostSearchedTextResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetMostSearchedTextResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetMostSearchedTextResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetMostSearchedTextResult"));
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
