/**
 * Generic_GetPendingPopular.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetPendingPopular  implements java.io.Serializable {
    private int p_Key;

    private long p_PartyID;

    private int p_ItemCategoryID;

    public Generic_GetPendingPopular() {
    }

    public Generic_GetPendingPopular(
           int p_Key,
           long p_PartyID,
           int p_ItemCategoryID) {
           this.p_Key = p_Key;
           this.p_PartyID = p_PartyID;
           this.p_ItemCategoryID = p_ItemCategoryID;
    }


    /**
     * Gets the p_Key value for this Generic_GetPendingPopular.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Generic_GetPendingPopular.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_PartyID value for this Generic_GetPendingPopular.
     * 
     * @return p_PartyID
     */
    public long getP_PartyID() {
        return p_PartyID;
    }


    /**
     * Sets the p_PartyID value for this Generic_GetPendingPopular.
     * 
     * @param p_PartyID
     */
    public void setP_PartyID(long p_PartyID) {
        this.p_PartyID = p_PartyID;
    }


    /**
     * Gets the p_ItemCategoryID value for this Generic_GetPendingPopular.
     * 
     * @return p_ItemCategoryID
     */
    public int getP_ItemCategoryID() {
        return p_ItemCategoryID;
    }


    /**
     * Sets the p_ItemCategoryID value for this Generic_GetPendingPopular.
     * 
     * @param p_ItemCategoryID
     */
    public void setP_ItemCategoryID(int p_ItemCategoryID) {
        this.p_ItemCategoryID = p_ItemCategoryID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetPendingPopular)) return false;
        Generic_GetPendingPopular other = (Generic_GetPendingPopular) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            this.p_PartyID == other.getP_PartyID() &&
            this.p_ItemCategoryID == other.getP_ItemCategoryID();
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
        _hashCode += new Long(getP_PartyID()).hashCode();
        _hashCode += getP_ItemCategoryID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetPendingPopular.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetPendingPopular"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_PartyID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_PartyID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ItemCategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ItemCategoryID"));
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
