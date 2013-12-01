/**
 * Item_RunGenericCFInBatchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_RunGenericCFInBatchResponse  implements java.io.Serializable {
    private String item_RunGenericCFInBatchResult;

    public Item_RunGenericCFInBatchResponse() {
    }

    public Item_RunGenericCFInBatchResponse(
           String item_RunGenericCFInBatchResult) {
           this.item_RunGenericCFInBatchResult = item_RunGenericCFInBatchResult;
    }


    /**
     * Gets the item_RunGenericCFInBatchResult value for this Item_RunGenericCFInBatchResponse.
     * 
     * @return item_RunGenericCFInBatchResult
     */
    public String getItem_RunGenericCFInBatchResult() {
        return item_RunGenericCFInBatchResult;
    }


    /**
     * Sets the item_RunGenericCFInBatchResult value for this Item_RunGenericCFInBatchResponse.
     * 
     * @param item_RunGenericCFInBatchResult
     */
    public void setItem_RunGenericCFInBatchResult(String item_RunGenericCFInBatchResult) {
        this.item_RunGenericCFInBatchResult = item_RunGenericCFInBatchResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_RunGenericCFInBatchResponse)) return false;
        Item_RunGenericCFInBatchResponse other = (Item_RunGenericCFInBatchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_RunGenericCFInBatchResult==null && other.getItem_RunGenericCFInBatchResult()==null) || 
             (this.item_RunGenericCFInBatchResult!=null &&
              this.item_RunGenericCFInBatchResult.equals(other.getItem_RunGenericCFInBatchResult())));
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
        if (getItem_RunGenericCFInBatchResult() != null) {
            _hashCode += getItem_RunGenericCFInBatchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_RunGenericCFInBatchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_RunGenericCFInBatchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_RunGenericCFInBatchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_RunGenericCFInBatchResult"));
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
