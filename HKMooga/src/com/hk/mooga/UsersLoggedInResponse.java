/**
 * UsersLoggedInResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class UsersLoggedInResponse  implements java.io.Serializable {
    private int usersLoggedInResult;

    public UsersLoggedInResponse() {
    }

    public UsersLoggedInResponse(
           int usersLoggedInResult) {
           this.usersLoggedInResult = usersLoggedInResult;
    }


    /**
     * Gets the usersLoggedInResult value for this UsersLoggedInResponse.
     * 
     * @return usersLoggedInResult
     */
    public int getUsersLoggedInResult() {
        return usersLoggedInResult;
    }


    /**
     * Sets the usersLoggedInResult value for this UsersLoggedInResponse.
     * 
     * @param usersLoggedInResult
     */
    public void setUsersLoggedInResult(int usersLoggedInResult) {
        this.usersLoggedInResult = usersLoggedInResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof UsersLoggedInResponse)) return false;
        UsersLoggedInResponse other = (UsersLoggedInResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.usersLoggedInResult == other.getUsersLoggedInResult();
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
        _hashCode += getUsersLoggedInResult();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UsersLoggedInResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">UsersLoggedInResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usersLoggedInResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "UsersLoggedInResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
