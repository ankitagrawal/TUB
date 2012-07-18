/**
 * Item_GetPopularBrandsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Item_GetPopularBrandsResponse  implements java.io.Serializable {
    private String item_GetPopularBrandsResult;

    public Item_GetPopularBrandsResponse() {
    }

    public Item_GetPopularBrandsResponse(
           String item_GetPopularBrandsResult) {
           this.item_GetPopularBrandsResult = item_GetPopularBrandsResult;
    }


    /**
     * Gets the item_GetPopularBrandsResult value for this Item_GetPopularBrandsResponse.
     * 
     * @return item_GetPopularBrandsResult
     */
    public String getItem_GetPopularBrandsResult() {
        return item_GetPopularBrandsResult;
    }


    /**
     * Sets the item_GetPopularBrandsResult value for this Item_GetPopularBrandsResponse.
     * 
     * @param item_GetPopularBrandsResult
     */
    public void setItem_GetPopularBrandsResult(String item_GetPopularBrandsResult) {
        this.item_GetPopularBrandsResult = item_GetPopularBrandsResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Item_GetPopularBrandsResponse)) return false;
        Item_GetPopularBrandsResponse other = (Item_GetPopularBrandsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item_GetPopularBrandsResult==null && other.getItem_GetPopularBrandsResult()==null) || 
             (this.item_GetPopularBrandsResult!=null &&
              this.item_GetPopularBrandsResult.equals(other.getItem_GetPopularBrandsResult())));
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
        if (getItem_GetPopularBrandsResult() != null) {
            _hashCode += getItem_GetPopularBrandsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Item_GetPopularBrandsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Item_GetPopularBrandsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item_GetPopularBrandsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Item_GetPopularBrandsResult"));
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
