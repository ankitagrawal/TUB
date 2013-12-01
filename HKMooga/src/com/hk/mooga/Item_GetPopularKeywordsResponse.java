/**
 * Item_GetPopularKeywordsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetPopularKeywordsResponse  implements java.io.Serializable {
    private String item_GetPopularKeywordsResult;

    public Item_GetPopularKeywordsResponse() {
    }

    public Item_GetPopularKeywordsResponse(
           String item_GetPopularKeywordsResult) {
           this.item_GetPopularKeywordsResult = item_GetPopularKeywordsResult;
    }


    /**
     * Gets the item_GetPopularKeywordsResult value for this Item_GetPopularKeywordsResponse.
     * 
     * @return item_GetPopularKeywordsResult
     */
    public String getItem_GetPopularKeywordsResult() {
        return item_GetPopularKeywordsResult;
    }


    /**
     * Sets the item_GetPopularKeywordsResult value for this Item_GetPopularKeywordsResponse.
     * 
     * @param item_GetPopularKeywordsResult
     */
    public void setItem_GetPopularKeywordsResult(String item_GetPopularKeywordsResult) {
        this.item_GetPopularKeywordsResult = item_GetPopularKeywordsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetPopularKeywordsResponse)) return false;
        Item_GetPopularKeywordsResponse other = (Item_GetPopularKeywordsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetPopularKeywordsResult==null && other.getItem_GetPopularKeywordsResult()==null) || 
             (this.item_GetPopularKeywordsResult!=null &&
              this.item_GetPopularKeywordsResult.equals(other.getItem_GetPopularKeywordsResult())));
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
        if (getItem_GetPopularKeywordsResult() != null) {
            _hashCode += getItem_GetPopularKeywordsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetPopularKeywordsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetPopularKeywordsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetPopularKeywordsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetPopularKeywordsResult"));
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
