/**
 * UserLoggedInResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class UserLoggedInResponse  implements java.io.Serializable {
    private java.lang.String userLoggedInResult;

    public UserLoggedInResponse() {
    }

    public UserLoggedInResponse(
           java.lang.String userLoggedInResult) {
           this.userLoggedInResult = userLoggedInResult;
    }


    /**
     * Gets the userLoggedInResult value for this UserLoggedInResponse.
     * 
     * @return userLoggedInResult
     */
    public java.lang.String getUserLoggedInResult() {
        return userLoggedInResult;
    }


    /**
     * Sets the userLoggedInResult value for this UserLoggedInResponse.
     * 
     * @param userLoggedInResult
     */
    public void setUserLoggedInResult(java.lang.String userLoggedInResult) {
        this.userLoggedInResult = userLoggedInResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserLoggedInResponse)) return false;
        UserLoggedInResponse other = (UserLoggedInResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userLoggedInResult==null && other.getUserLoggedInResult()==null) || 
             (this.userLoggedInResult!=null &&
              this.userLoggedInResult.equals(other.getUserLoggedInResult())));
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
        if (getUserLoggedInResult() != null) {
            _hashCode += getUserLoggedInResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserLoggedInResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">UserLoggedInResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userLoggedInResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "UserLoggedInResult"));
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
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
