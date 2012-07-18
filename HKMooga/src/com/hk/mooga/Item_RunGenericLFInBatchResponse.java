/**
 * Item_RunGenericLFInBatchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_RunGenericLFInBatchResponse  implements java.io.Serializable {
    private String item_RunGenericLFInBatchResult;

    public Item_RunGenericLFInBatchResponse() {
    }

    public Item_RunGenericLFInBatchResponse(
           String item_RunGenericLFInBatchResult) {
           this.item_RunGenericLFInBatchResult = item_RunGenericLFInBatchResult;
    }


    /**
     * Gets the item_RunGenericLFInBatchResult value for this Item_RunGenericLFInBatchResponse.
     * 
     * @return item_RunGenericLFInBatchResult
     */
    public String getItem_RunGenericLFInBatchResult() {
        return item_RunGenericLFInBatchResult;
    }


    /**
     * Sets the item_RunGenericLFInBatchResult value for this Item_RunGenericLFInBatchResponse.
     * 
     * @param item_RunGenericLFInBatchResult
     */
    public void setItem_RunGenericLFInBatchResult(String item_RunGenericLFInBatchResult) {
        this.item_RunGenericLFInBatchResult = item_RunGenericLFInBatchResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_RunGenericLFInBatchResponse)) return false;
        Item_RunGenericLFInBatchResponse other = (Item_RunGenericLFInBatchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_RunGenericLFInBatchResult==null && other.getItem_RunGenericLFInBatchResult()==null) || 
             (this.item_RunGenericLFInBatchResult!=null &&
              this.item_RunGenericLFInBatchResult.equals(other.getItem_RunGenericLFInBatchResult())));
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
        if (getItem_RunGenericLFInBatchResult() != null) {
            _hashCode += getItem_RunGenericLFInBatchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_RunGenericLFInBatchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_RunGenericLFInBatchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_RunGenericLFInBatchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_RunGenericLFInBatchResult"));
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
