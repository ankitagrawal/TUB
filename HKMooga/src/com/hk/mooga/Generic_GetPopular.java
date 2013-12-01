/**
 * Generic_GetPopular.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetPopular  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtPartyID;

    private String p_ExtItemCategoryID;

    private String p_ServiceNameNCode;

    private String p_ExtLocationID;

    public Generic_GetPopular() {
    }

    public Generic_GetPopular(
           int p_Key,
           String p_ExtPartyID,
           String p_ExtItemCategoryID,
           String p_ServiceNameNCode,
           String p_ExtLocationID) {
           this.p_Key = p_Key;
           this.p_ExtPartyID = p_ExtPartyID;
           this.p_ExtItemCategoryID = p_ExtItemCategoryID;
           this.p_ServiceNameNCode = p_ServiceNameNCode;
           this.p_ExtLocationID = p_ExtLocationID;
    }


    /**
     * Gets the p_Key value for this Generic_GetPopular.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Generic_GetPopular.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtPartyID value for this Generic_GetPopular.
     * 
     * @return p_ExtPartyID
     */
    public String getP_ExtPartyID() {
        return p_ExtPartyID;
    }


    /**
     * Sets the p_ExtPartyID value for this Generic_GetPopular.
     * 
     * @param p_ExtPartyID
     */
    public void setP_ExtPartyID(String p_ExtPartyID) {
        this.p_ExtPartyID = p_ExtPartyID;
    }


    /**
     * Gets the p_ExtItemCategoryID value for this Generic_GetPopular.
     * 
     * @return p_ExtItemCategoryID
     */
    public String getP_ExtItemCategoryID() {
        return p_ExtItemCategoryID;
    }


    /**
     * Sets the p_ExtItemCategoryID value for this Generic_GetPopular.
     * 
     * @param p_ExtItemCategoryID
     */
    public void setP_ExtItemCategoryID(String p_ExtItemCategoryID) {
        this.p_ExtItemCategoryID = p_ExtItemCategoryID;
    }


    /**
     * Gets the p_ServiceNameNCode value for this Generic_GetPopular.
     * 
     * @return p_ServiceNameNCode
     */
    public String getP_ServiceNameNCode() {
        return p_ServiceNameNCode;
    }


    /**
     * Sets the p_ServiceNameNCode value for this Generic_GetPopular.
     * 
     * @param p_ServiceNameNCode
     */
    public void setP_ServiceNameNCode(String p_ServiceNameNCode) {
        this.p_ServiceNameNCode = p_ServiceNameNCode;
    }


    /**
     * Gets the p_ExtLocationID value for this Generic_GetPopular.
     * 
     * @return p_ExtLocationID
     */
    public String getP_ExtLocationID() {
        return p_ExtLocationID;
    }


    /**
     * Sets the p_ExtLocationID value for this Generic_GetPopular.
     * 
     * @param p_ExtLocationID
     */
    public void setP_ExtLocationID(String p_ExtLocationID) {
        this.p_ExtLocationID = p_ExtLocationID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetPopular)) return false;
        Generic_GetPopular other = (Generic_GetPopular) obj;
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
            ((this.p_ExtItemCategoryID==null && other.getP_ExtItemCategoryID()==null) || 
             (this.p_ExtItemCategoryID!=null &&
              this.p_ExtItemCategoryID.equals(other.getP_ExtItemCategoryID()))) &&
            ((this.p_ServiceNameNCode==null && other.getP_ServiceNameNCode()==null) || 
             (this.p_ServiceNameNCode!=null &&
              this.p_ServiceNameNCode.equals(other.getP_ServiceNameNCode()))) &&
            ((this.p_ExtLocationID==null && other.getP_ExtLocationID()==null) || 
             (this.p_ExtLocationID!=null &&
              this.p_ExtLocationID.equals(other.getP_ExtLocationID())));
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
        if (getP_ExtItemCategoryID() != null) {
            _hashCode += getP_ExtItemCategoryID().hashCode();
        }
        if (getP_ServiceNameNCode() != null) {
            _hashCode += getP_ServiceNameNCode().hashCode();
        }
        if (getP_ExtLocationID() != null) {
            _hashCode += getP_ExtLocationID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetPopular.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetPopular"));
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
        elemField.setFieldName("p_ExtItemCategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtItemCategoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceNameNCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceNameNCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtLocationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtLocationID"));
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
