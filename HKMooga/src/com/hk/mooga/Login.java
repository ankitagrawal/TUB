/**
 * Login.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Login  implements java.io.Serializable {
    private String p_User;

    private String p_Application;

    public Login() {
    }

    public Login(
           String p_User,
           String p_Application) {
           this.p_User = p_User;
           this.p_Application = p_Application;
    }


    /**
     * Gets the p_User value for this Login.
     * 
     * @return p_User
     */
    public String getP_User() {
        return p_User;
    }


    /**
     * Sets the p_User value for this Login.
     * 
     * @param p_User
     */
    public void setP_User(String p_User) {
        this.p_User = p_User;
    }


    /**
     * Gets the p_Application value for this Login.
     * 
     * @return p_Application
     */
    public String getP_Application() {
        return p_Application;
    }


    /**
     * Sets the p_Application value for this Login.
     * 
     * @param p_Application
     */
    public void setP_Application(String p_Application) {
        this.p_Application = p_Application;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Login)) return false;
        Login other = (Login) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.p_User==null && other.getP_User()==null) || 
             (this.p_User!=null &&
              this.p_User.equals(other.getP_User()))) &&
            ((this.p_Application==null && other.getP_Application()==null) || 
             (this.p_Application!=null &&
              this.p_Application.equals(other.getP_Application())));
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
        if (getP_User() != null) {
            _hashCode += getP_User().hashCode();
        }
        if (getP_Application() != null) {
            _hashCode += getP_Application().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Login.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Login"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_User");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_User"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Application");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Application"));
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
