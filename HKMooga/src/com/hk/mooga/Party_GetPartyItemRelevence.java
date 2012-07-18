/**
 * Party_GetPartyItemRelevence.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GetPartyItemRelevence  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtPartyID;

    private String p_ExtItemID;

    private String p_ExtCatID;

    private String p_ServiceCode;

    public Party_GetPartyItemRelevence() {
    }

    public Party_GetPartyItemRelevence(
           int p_Key,
           String p_ExtPartyID,
           String p_ExtItemID,
           String p_ExtCatID,
           String p_ServiceCode) {
           this.p_Key = p_Key;
           this.p_ExtPartyID = p_ExtPartyID;
           this.p_ExtItemID = p_ExtItemID;
           this.p_ExtCatID = p_ExtCatID;
           this.p_ServiceCode = p_ServiceCode;
    }


    /**
     * Gets the p_Key value for this Party_GetPartyItemRelevence.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Party_GetPartyItemRelevence.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtPartyID value for this Party_GetPartyItemRelevence.
     * 
     * @return p_ExtPartyID
     */
    public String getP_ExtPartyID() {
        return p_ExtPartyID;
    }


    /**
     * Sets the p_ExtPartyID value for this Party_GetPartyItemRelevence.
     * 
     * @param p_ExtPartyID
     */
    public void setP_ExtPartyID(String p_ExtPartyID) {
        this.p_ExtPartyID = p_ExtPartyID;
    }


    /**
     * Gets the p_ExtItemID value for this Party_GetPartyItemRelevence.
     * 
     * @return p_ExtItemID
     */
    public String getP_ExtItemID() {
        return p_ExtItemID;
    }


    /**
     * Sets the p_ExtItemID value for this Party_GetPartyItemRelevence.
     * 
     * @param p_ExtItemID
     */
    public void setP_ExtItemID(String p_ExtItemID) {
        this.p_ExtItemID = p_ExtItemID;
    }


    /**
     * Gets the p_ExtCatID value for this Party_GetPartyItemRelevence.
     * 
     * @return p_ExtCatID
     */
    public String getP_ExtCatID() {
        return p_ExtCatID;
    }


    /**
     * Sets the p_ExtCatID value for this Party_GetPartyItemRelevence.
     * 
     * @param p_ExtCatID
     */
    public void setP_ExtCatID(String p_ExtCatID) {
        this.p_ExtCatID = p_ExtCatID;
    }


    /**
     * Gets the p_ServiceCode value for this Party_GetPartyItemRelevence.
     * 
     * @return p_ServiceCode
     */
    public String getP_ServiceCode() {
        return p_ServiceCode;
    }


    /**
     * Sets the p_ServiceCode value for this Party_GetPartyItemRelevence.
     * 
     * @param p_ServiceCode
     */
    public void setP_ServiceCode(String p_ServiceCode) {
        this.p_ServiceCode = p_ServiceCode;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GetPartyItemRelevence)) return false;
        Party_GetPartyItemRelevence other = (Party_GetPartyItemRelevence) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtPartyID==null && other.getP_ExtPartyID()==null) || 
             (this.p_ExtPartyID!=null &&
              this.p_ExtPartyID.equals(other.getP_ExtPartyID()))) &&
            ((this.p_ExtItemID==null && other.getP_ExtItemID()==null) || 
             (this.p_ExtItemID!=null &&
              this.p_ExtItemID.equals(other.getP_ExtItemID()))) &&
            ((this.p_ExtCatID==null && other.getP_ExtCatID()==null) || 
             (this.p_ExtCatID!=null &&
              this.p_ExtCatID.equals(other.getP_ExtCatID()))) &&
            ((this.p_ServiceCode==null && other.getP_ServiceCode()==null) || 
             (this.p_ServiceCode!=null &&
              this.p_ServiceCode.equals(other.getP_ServiceCode())));
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
        if (getP_ExtPartyID() != null) {
            _hashCode += getP_ExtPartyID().hashCode();
        }
        if (getP_ExtItemID() != null) {
            _hashCode += getP_ExtItemID().hashCode();
        }
        if (getP_ExtCatID() != null) {
            _hashCode += getP_ExtCatID().hashCode();
        }
        if (getP_ServiceCode() != null) {
            _hashCode += getP_ServiceCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GetPartyItemRelevence.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GetPartyItemRelevence"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtPartyID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtPartyID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtCatID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtCatID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceCode"));
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
