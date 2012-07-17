/**
 * Category_RunGenericCFInBatchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Category_RunGenericCFInBatchResponse  implements java.io.Serializable {
    private String category_RunGenericCFInBatchResult;

    public Category_RunGenericCFInBatchResponse() {
    }

    public Category_RunGenericCFInBatchResponse(
           String category_RunGenericCFInBatchResult) {
           this.category_RunGenericCFInBatchResult = category_RunGenericCFInBatchResult;
    }


    /**
     * Gets the category_RunGenericCFInBatchResult value for this Category_RunGenericCFInBatchResponse.
     * 
     * @return category_RunGenericCFInBatchResult
     */
    public String getCategory_RunGenericCFInBatchResult() {
        return category_RunGenericCFInBatchResult;
    }


    /**
     * Sets the category_RunGenericCFInBatchResult value for this Category_RunGenericCFInBatchResponse.
     * 
     * @param category_RunGenericCFInBatchResult
     */
    public void setCategory_RunGenericCFInBatchResult(String category_RunGenericCFInBatchResult) {
        this.category_RunGenericCFInBatchResult = category_RunGenericCFInBatchResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Category_RunGenericCFInBatchResponse)) return false;
        Category_RunGenericCFInBatchResponse other = (Category_RunGenericCFInBatchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.category_RunGenericCFInBatchResult==null && other.getCategory_RunGenericCFInBatchResult()==null) || 
             (this.category_RunGenericCFInBatchResult!=null &&
              this.category_RunGenericCFInBatchResult.equals(other.getCategory_RunGenericCFInBatchResult())));
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
        if (getCategory_RunGenericCFInBatchResult() != null) {
            _hashCode += getCategory_RunGenericCFInBatchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Category_RunGenericCFInBatchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Category_RunGenericCFInBatchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category_RunGenericCFInBatchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Category_RunGenericCFInBatchResult"));
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
