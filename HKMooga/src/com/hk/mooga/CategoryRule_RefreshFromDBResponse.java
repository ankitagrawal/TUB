/**
 * CategoryRule_RefreshFromDBResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class CategoryRule_RefreshFromDBResponse  implements java.io.Serializable {
    private String categoryRule_RefreshFromDBResult;

    public CategoryRule_RefreshFromDBResponse() {
    }

    public CategoryRule_RefreshFromDBResponse(
           String categoryRule_RefreshFromDBResult) {
           this.categoryRule_RefreshFromDBResult = categoryRule_RefreshFromDBResult;
    }


    /**
     * Gets the categoryRule_RefreshFromDBResult value for this CategoryRule_RefreshFromDBResponse.
     * 
     * @return categoryRule_RefreshFromDBResult
     */
    public String getCategoryRule_RefreshFromDBResult() {
        return categoryRule_RefreshFromDBResult;
    }


    /**
     * Sets the categoryRule_RefreshFromDBResult value for this CategoryRule_RefreshFromDBResponse.
     * 
     * @param categoryRule_RefreshFromDBResult
     */
    public void setCategoryRule_RefreshFromDBResult(String categoryRule_RefreshFromDBResult) {
        this.categoryRule_RefreshFromDBResult = categoryRule_RefreshFromDBResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof CategoryRule_RefreshFromDBResponse)) return false;
        CategoryRule_RefreshFromDBResponse other = (CategoryRule_RefreshFromDBResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categoryRule_RefreshFromDBResult==null && other.getCategoryRule_RefreshFromDBResult()==null) || 
             (this.categoryRule_RefreshFromDBResult!=null &&
              this.categoryRule_RefreshFromDBResult.equals(other.getCategoryRule_RefreshFromDBResult())));
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
        if (getCategoryRule_RefreshFromDBResult() != null) {
            _hashCode += getCategoryRule_RefreshFromDBResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CategoryRule_RefreshFromDBResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">CategoryRule_RefreshFromDBResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryRule_RefreshFromDBResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "CategoryRule_RefreshFromDBResult"));
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
