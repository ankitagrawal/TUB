/**
 * Category_GetAllCategoriesInTextResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Category_GetAllCategoriesInTextResponse  implements java.io.Serializable {
    private String[] category_GetAllCategoriesInTextResult;

    public Category_GetAllCategoriesInTextResponse() {
    }

    public Category_GetAllCategoriesInTextResponse(
           String[] category_GetAllCategoriesInTextResult) {
           this.category_GetAllCategoriesInTextResult = category_GetAllCategoriesInTextResult;
    }


    /**
     * Gets the category_GetAllCategoriesInTextResult value for this Category_GetAllCategoriesInTextResponse.
     * 
     * @return category_GetAllCategoriesInTextResult
     */
    public String[] getCategory_GetAllCategoriesInTextResult() {
        return category_GetAllCategoriesInTextResult;
    }


    /**
     * Sets the category_GetAllCategoriesInTextResult value for this Category_GetAllCategoriesInTextResponse.
     * 
     * @param category_GetAllCategoriesInTextResult
     */
    public void setCategory_GetAllCategoriesInTextResult(String[] category_GetAllCategoriesInTextResult) {
        this.category_GetAllCategoriesInTextResult = category_GetAllCategoriesInTextResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Category_GetAllCategoriesInTextResponse)) return false;
        Category_GetAllCategoriesInTextResponse other = (Category_GetAllCategoriesInTextResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.category_GetAllCategoriesInTextResult==null && other.getCategory_GetAllCategoriesInTextResult()==null) || 
             (this.category_GetAllCategoriesInTextResult!=null &&
              java.util.Arrays.equals(this.category_GetAllCategoriesInTextResult, other.getCategory_GetAllCategoriesInTextResult())));
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
        if (getCategory_GetAllCategoriesInTextResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCategory_GetAllCategoriesInTextResult());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getCategory_GetAllCategoriesInTextResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Category_GetAllCategoriesInTextResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Category_GetAllCategoriesInTextResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category_GetAllCategoriesInTextResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Category_GetAllCategoriesInTextResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "string"));
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
