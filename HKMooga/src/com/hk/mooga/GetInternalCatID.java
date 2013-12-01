/**
 * GetInternalCatID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class GetInternalCatID  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtCategoryID;

    private String p_CatIDType;

    public GetInternalCatID() {
    }

    public GetInternalCatID(
           int p_Key,
           String p_ExtCategoryID,
           String p_CatIDType) {
           this.p_Key = p_Key;
           this.p_ExtCategoryID = p_ExtCategoryID;
           this.p_CatIDType = p_CatIDType;
    }


    /**
     * Gets the p_Key value for this GetInternalCatID.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this GetInternalCatID.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtCategoryID value for this GetInternalCatID.
     * 
     * @return p_ExtCategoryID
     */
    public String getP_ExtCategoryID() {
        return p_ExtCategoryID;
    }


    /**
     * Sets the p_ExtCategoryID value for this GetInternalCatID.
     * 
     * @param p_ExtCategoryID
     */
    public void setP_ExtCategoryID(String p_ExtCategoryID) {
        this.p_ExtCategoryID = p_ExtCategoryID;
    }


    /**
     * Gets the p_CatIDType value for this GetInternalCatID.
     * 
     * @return p_CatIDType
     */
    public String getP_CatIDType() {
        return p_CatIDType;
    }


    /**
     * Sets the p_CatIDType value for this GetInternalCatID.
     * 
     * @param p_CatIDType
     */
    public void setP_CatIDType(String p_CatIDType) {
        this.p_CatIDType = p_CatIDType;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetInternalCatID)) return false;
        GetInternalCatID other = (GetInternalCatID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtCategoryID==null && other.getP_ExtCategoryID()==null) || 
             (this.p_ExtCategoryID!=null &&
              this.p_ExtCategoryID.equals(other.getP_ExtCategoryID()))) &&
            ((this.p_CatIDType==null && other.getP_CatIDType()==null) || 
             (this.p_CatIDType!=null &&
              this.p_CatIDType.equals(other.getP_CatIDType())));
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
        if (getP_ExtCategoryID() != null) {
            _hashCode += getP_ExtCategoryID().hashCode();
        }
        if (getP_CatIDType() != null) {
            _hashCode += getP_CatIDType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetInternalCatID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">GetInternalCatID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtCategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtCategoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_CatIDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_CatIDType"));
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
