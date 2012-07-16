/**
 * Item_RunGenericCFInBatchForKeywordsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_RunGenericCFInBatchForKeywordsResponse  implements java.io.Serializable {
    private String item_RunGenericCFInBatchForKeywordsResult;

    public Item_RunGenericCFInBatchForKeywordsResponse() {
    }

    public Item_RunGenericCFInBatchForKeywordsResponse(
           String item_RunGenericCFInBatchForKeywordsResult) {
           this.item_RunGenericCFInBatchForKeywordsResult = item_RunGenericCFInBatchForKeywordsResult;
    }


    /**
     * Gets the item_RunGenericCFInBatchForKeywordsResult value for this Item_RunGenericCFInBatchForKeywordsResponse.
     * 
     * @return item_RunGenericCFInBatchForKeywordsResult
     */
    public String getItem_RunGenericCFInBatchForKeywordsResult() {
        return item_RunGenericCFInBatchForKeywordsResult;
    }


    /**
     * Sets the item_RunGenericCFInBatchForKeywordsResult value for this Item_RunGenericCFInBatchForKeywordsResponse.
     * 
     * @param item_RunGenericCFInBatchForKeywordsResult
     */
    public void setItem_RunGenericCFInBatchForKeywordsResult(String item_RunGenericCFInBatchForKeywordsResult) {
        this.item_RunGenericCFInBatchForKeywordsResult = item_RunGenericCFInBatchForKeywordsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_RunGenericCFInBatchForKeywordsResponse)) return false;
        Item_RunGenericCFInBatchForKeywordsResponse other = (Item_RunGenericCFInBatchForKeywordsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_RunGenericCFInBatchForKeywordsResult==null && other.getItem_RunGenericCFInBatchForKeywordsResult()==null) || 
             (this.item_RunGenericCFInBatchForKeywordsResult!=null &&
              this.item_RunGenericCFInBatchForKeywordsResult.equals(other.getItem_RunGenericCFInBatchForKeywordsResult())));
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
        if (getItem_RunGenericCFInBatchForKeywordsResult() != null) {
            _hashCode += getItem_RunGenericCFInBatchForKeywordsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_RunGenericCFInBatchForKeywordsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_RunGenericCFInBatchForKeywordsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_RunGenericCFInBatchForKeywordsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_RunGenericCFInBatchForKeywordsResult"));
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
