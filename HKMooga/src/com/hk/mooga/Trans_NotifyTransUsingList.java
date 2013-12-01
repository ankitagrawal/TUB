/**
 * Trans_NotifyTransUsingList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Trans_NotifyTransUsingList  implements java.io.Serializable {
    private int p_Key;

    private String p_UDFName;

    private String[] p_ArgumentList;

    public Trans_NotifyTransUsingList() {
    }

    public Trans_NotifyTransUsingList(
           int p_Key,
           String p_UDFName,
           String[] p_ArgumentList) {
           this.p_Key = p_Key;
           this.p_UDFName = p_UDFName;
           this.p_ArgumentList = p_ArgumentList;
    }


    /**
     * Gets the p_Key value for this Trans_NotifyTransUsingList.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Trans_NotifyTransUsingList.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_UDFName value for this Trans_NotifyTransUsingList.
     * 
     * @return p_UDFName
     */
    public String getP_UDFName() {
        return p_UDFName;
    }


    /**
     * Sets the p_UDFName value for this Trans_NotifyTransUsingList.
     * 
     * @param p_UDFName
     */
    public void setP_UDFName(String p_UDFName) {
        this.p_UDFName = p_UDFName;
    }


    /**
     * Gets the p_ArgumentList value for this Trans_NotifyTransUsingList.
     * 
     * @return p_ArgumentList
     */
    public String[] getP_ArgumentList() {
        return p_ArgumentList;
    }


    /**
     * Sets the p_ArgumentList value for this Trans_NotifyTransUsingList.
     * 
     * @param p_ArgumentList
     */
    public void setP_ArgumentList(String[] p_ArgumentList) {
        this.p_ArgumentList = p_ArgumentList;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Trans_NotifyTransUsingList)) return false;
        Trans_NotifyTransUsingList other = (Trans_NotifyTransUsingList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_UDFName==null && other.getP_UDFName()==null) || 
             (this.p_UDFName!=null &&
              this.p_UDFName.equals(other.getP_UDFName()))) &&
            ((this.p_ArgumentList==null && other.getP_ArgumentList()==null) || 
             (this.p_ArgumentList!=null &&
              java.util.Arrays.equals(this.p_ArgumentList, other.getP_ArgumentList())));
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
        _hashCode += getP_Key();
        if (getP_UDFName() != null) {
            _hashCode += getP_UDFName().hashCode();
        }
        if (getP_ArgumentList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getP_ArgumentList());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getP_ArgumentList(), i);
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
        new org.apache.axis.description.TypeDesc(Trans_NotifyTransUsingList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Trans_NotifyTransUsingList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_UDFName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_UDFName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ArgumentList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ArgumentList"));
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
