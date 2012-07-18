/**
 * ExecuteWebServiceUsingList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteWebServiceUsingList  implements java.io.Serializable {
    private int p_Key;

    private String p_WebServiceName;

    private Object[] p_Parameters;

    public ExecuteWebServiceUsingList() {
    }

    public ExecuteWebServiceUsingList(
           int p_Key,
           String p_WebServiceName,
           Object[] p_Parameters) {
           this.p_Key = p_Key;
           this.p_WebServiceName = p_WebServiceName;
           this.p_Parameters = p_Parameters;
    }


    /**
     * Gets the p_Key value for this ExecuteWebServiceUsingList.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this ExecuteWebServiceUsingList.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_WebServiceName value for this ExecuteWebServiceUsingList.
     * 
     * @return p_WebServiceName
     */
    public String getP_WebServiceName() {
        return p_WebServiceName;
    }


    /**
     * Sets the p_WebServiceName value for this ExecuteWebServiceUsingList.
     * 
     * @param p_WebServiceName
     */
    public void setP_WebServiceName(String p_WebServiceName) {
        this.p_WebServiceName = p_WebServiceName;
    }


    /**
     * Gets the p_Parameters value for this ExecuteWebServiceUsingList.
     * 
     * @return p_Parameters
     */
    public Object[] getP_Parameters() {
        return p_Parameters;
    }


    /**
     * Sets the p_Parameters value for this ExecuteWebServiceUsingList.
     * 
     * @param p_Parameters
     */
    public void setP_Parameters(Object[] p_Parameters) {
        this.p_Parameters = p_Parameters;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteWebServiceUsingList)) return false;
        ExecuteWebServiceUsingList other = (ExecuteWebServiceUsingList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_WebServiceName==null && other.getP_WebServiceName()==null) || 
             (this.p_WebServiceName!=null &&
              this.p_WebServiceName.equals(other.getP_WebServiceName()))) &&
            ((this.p_Parameters==null && other.getP_Parameters()==null) || 
             (this.p_Parameters!=null &&
              java.util.Arrays.equals(this.p_Parameters, other.getP_Parameters())));
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
        if (getP_WebServiceName() != null) {
            _hashCode += getP_WebServiceName().hashCode();
        }
        if (getP_Parameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getP_Parameters());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getP_Parameters(), i);
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
        new org.apache.axis.description.TypeDesc(ExecuteWebServiceUsingList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteWebServiceUsingList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_WebServiceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_WebServiceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "anyType"));
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
