/**
 * Party_SetPartyStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_SetPartyStatus  implements java.io.Serializable {
    private int p_Key;

    private String p_TempExtPartyID;

    private String p_ExtPartyID;

    private String p_PartyName;

    private String p_Type;

    private String p_Status;

    public Party_SetPartyStatus() {
    }

    public Party_SetPartyStatus(
           int p_Key,
           String p_TempExtPartyID,
           String p_ExtPartyID,
           String p_PartyName,
           String p_Type,
           String p_Status) {
           this.p_Key = p_Key;
           this.p_TempExtPartyID = p_TempExtPartyID;
           this.p_ExtPartyID = p_ExtPartyID;
           this.p_PartyName = p_PartyName;
           this.p_Type = p_Type;
           this.p_Status = p_Status;
    }


    /**
     * Gets the p_Key value for this Party_SetPartyStatus.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Party_SetPartyStatus.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_TempExtPartyID value for this Party_SetPartyStatus.
     * 
     * @return p_TempExtPartyID
     */
    public String getP_TempExtPartyID() {
        return p_TempExtPartyID;
    }


    /**
     * Sets the p_TempExtPartyID value for this Party_SetPartyStatus.
     * 
     * @param p_TempExtPartyID
     */
    public void setP_TempExtPartyID(String p_TempExtPartyID) {
        this.p_TempExtPartyID = p_TempExtPartyID;
    }


    /**
     * Gets the p_ExtPartyID value for this Party_SetPartyStatus.
     * 
     * @return p_ExtPartyID
     */
    public String getP_ExtPartyID() {
        return p_ExtPartyID;
    }


    /**
     * Sets the p_ExtPartyID value for this Party_SetPartyStatus.
     * 
     * @param p_ExtPartyID
     */
    public void setP_ExtPartyID(String p_ExtPartyID) {
        this.p_ExtPartyID = p_ExtPartyID;
    }


    /**
     * Gets the p_PartyName value for this Party_SetPartyStatus.
     * 
     * @return p_PartyName
     */
    public String getP_PartyName() {
        return p_PartyName;
    }


    /**
     * Sets the p_PartyName value for this Party_SetPartyStatus.
     * 
     * @param p_PartyName
     */
    public void setP_PartyName(String p_PartyName) {
        this.p_PartyName = p_PartyName;
    }


    /**
     * Gets the p_Type value for this Party_SetPartyStatus.
     * 
     * @return p_Type
     */
    public String getP_Type() {
        return p_Type;
    }


    /**
     * Sets the p_Type value for this Party_SetPartyStatus.
     * 
     * @param p_Type
     */
    public void setP_Type(String p_Type) {
        this.p_Type = p_Type;
    }


    /**
     * Gets the p_Status value for this Party_SetPartyStatus.
     * 
     * @return p_Status
     */
    public String getP_Status() {
        return p_Status;
    }


    /**
     * Sets the p_Status value for this Party_SetPartyStatus.
     * 
     * @param p_Status
     */
    public void setP_Status(String p_Status) {
        this.p_Status = p_Status;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_SetPartyStatus)) return false;
        Party_SetPartyStatus other = (Party_SetPartyStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_TempExtPartyID==null && other.getP_TempExtPartyID()==null) || 
             (this.p_TempExtPartyID!=null &&
              this.p_TempExtPartyID.equals(other.getP_TempExtPartyID()))) &&
            ((this.p_ExtPartyID==null && other.getP_ExtPartyID()==null) || 
             (this.p_ExtPartyID!=null &&
              this.p_ExtPartyID.equals(other.getP_ExtPartyID()))) &&
            ((this.p_PartyName==null && other.getP_PartyName()==null) || 
             (this.p_PartyName!=null &&
              this.p_PartyName.equals(other.getP_PartyName()))) &&
            ((this.p_Type==null && other.getP_Type()==null) || 
             (this.p_Type!=null &&
              this.p_Type.equals(other.getP_Type()))) &&
            ((this.p_Status==null && other.getP_Status()==null) || 
             (this.p_Status!=null &&
              this.p_Status.equals(other.getP_Status())));
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
        if (getP_TempExtPartyID() != null) {
            _hashCode += getP_TempExtPartyID().hashCode();
        }
        if (getP_ExtPartyID() != null) {
            _hashCode += getP_ExtPartyID().hashCode();
        }
        if (getP_PartyName() != null) {
            _hashCode += getP_PartyName().hashCode();
        }
        if (getP_Type() != null) {
            _hashCode += getP_Type().hashCode();
        }
        if (getP_Status() != null) {
            _hashCode += getP_Status().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_SetPartyStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_SetPartyStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_TempExtPartyID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_TempExtPartyID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("p_PartyName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_PartyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Status"));
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
