/**
 * CategoryRule_GetRuleResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class CategoryRule_GetRuleResponse  implements java.io.Serializable {
    private float[] categoryRule_GetRuleResult;

    public CategoryRule_GetRuleResponse() {
    }

    public CategoryRule_GetRuleResponse(
           float[] categoryRule_GetRuleResult) {
           this.categoryRule_GetRuleResult = categoryRule_GetRuleResult;
    }


    /**
     * Gets the categoryRule_GetRuleResult value for this CategoryRule_GetRuleResponse.
     * 
     * @return categoryRule_GetRuleResult
     */
    public float[] getCategoryRule_GetRuleResult() {
        return categoryRule_GetRuleResult;
    }


    /**
     * Sets the categoryRule_GetRuleResult value for this CategoryRule_GetRuleResponse.
     * 
     * @param categoryRule_GetRuleResult
     */
    public void setCategoryRule_GetRuleResult(float[] categoryRule_GetRuleResult) {
        this.categoryRule_GetRuleResult = categoryRule_GetRuleResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof CategoryRule_GetRuleResponse)) return false;
        CategoryRule_GetRuleResponse other = (CategoryRule_GetRuleResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categoryRule_GetRuleResult==null && other.getCategoryRule_GetRuleResult()==null) || 
             (this.categoryRule_GetRuleResult!=null &&
              java.util.Arrays.equals(this.categoryRule_GetRuleResult, other.getCategoryRule_GetRuleResult())));
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
        if (getCategoryRule_GetRuleResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCategoryRule_GetRuleResult());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getCategoryRule_GetRuleResult(), i);
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
        new org.apache.axis.description.TypeDesc(CategoryRule_GetRuleResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">CategoryRule_GetRuleResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryRule_GetRuleResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "CategoryRule_GetRuleResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "float"));
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
