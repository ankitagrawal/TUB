/**
 * Category_RefreshFromDBResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Category_RefreshFromDBResponse  implements java.io.Serializable {
    private String category_RefreshFromDBResult;

    public Category_RefreshFromDBResponse() {
    }

    public Category_RefreshFromDBResponse(
           String category_RefreshFromDBResult) {
           this.category_RefreshFromDBResult = category_RefreshFromDBResult;
    }


    /**
     * Gets the category_RefreshFromDBResult value for this Category_RefreshFromDBResponse.
     * 
     * @return category_RefreshFromDBResult
     */
    public String getCategory_RefreshFromDBResult() {
        return category_RefreshFromDBResult;
    }


    /**
     * Sets the category_RefreshFromDBResult value for this Category_RefreshFromDBResponse.
     * 
     * @param category_RefreshFromDBResult
     */
    public void setCategory_RefreshFromDBResult(String category_RefreshFromDBResult) {
        this.category_RefreshFromDBResult = category_RefreshFromDBResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Category_RefreshFromDBResponse)) return false;
        Category_RefreshFromDBResponse other = (Category_RefreshFromDBResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.category_RefreshFromDBResult==null && other.getCategory_RefreshFromDBResult()==null) || 
             (this.category_RefreshFromDBResult!=null &&
              this.category_RefreshFromDBResult.equals(other.getCategory_RefreshFromDBResult())));
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
        if (getCategory_RefreshFromDBResult() != null) {
            _hashCode += getCategory_RefreshFromDBResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Category_RefreshFromDBResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Category_RefreshFromDBResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category_RefreshFromDBResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Category_RefreshFromDBResult"));
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
