/**
 * GetInternalID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class GetInternalID  implements java.io.Serializable {
    private int p_Key;

    private java.lang.String p_ExtID;

    private java.lang.String p_IDType;

    public GetInternalID() {
    }

    public GetInternalID(
           int p_Key,
           java.lang.String p_ExtID,
           java.lang.String p_IDType) {
           this.p_Key = p_Key;
           this.p_ExtID = p_ExtID;
           this.p_IDType = p_IDType;
    }


    /**
     * Gets the p_Key value for this GetInternalID.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this GetInternalID.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtID value for this GetInternalID.
     * 
     * @return p_ExtID
     */
    public java.lang.String getP_ExtID() {
        return p_ExtID;
    }


    /**
     * Sets the p_ExtID value for this GetInternalID.
     * 
     * @param p_ExtID
     */
    public void setP_ExtID(java.lang.String p_ExtID) {
        this.p_ExtID = p_ExtID;
    }


    /**
     * Gets the p_IDType value for this GetInternalID.
     * 
     * @return p_IDType
     */
    public java.lang.String getP_IDType() {
        return p_IDType;
    }


    /**
     * Sets the p_IDType value for this GetInternalID.
     * 
     * @param p_IDType
     */
    public void setP_IDType(java.lang.String p_IDType) {
        this.p_IDType = p_IDType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetInternalID)) return false;
        GetInternalID other = (GetInternalID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtID==null && other.getP_ExtID()==null) || 
             (this.p_ExtID!=null &&
              this.p_ExtID.equals(other.getP_ExtID()))) &&
            ((this.p_IDType==null && other.getP_IDType()==null) || 
             (this.p_IDType!=null &&
              this.p_IDType.equals(other.getP_IDType())));
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
        if (getP_ExtID() != null) {
            _hashCode += getP_ExtID().hashCode();
        }
        if (getP_IDType() != null) {
            _hashCode += getP_IDType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetInternalID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">GetInternalID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_IDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_IDType"));
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
