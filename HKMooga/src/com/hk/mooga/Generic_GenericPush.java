/**
 * Generic_GenericPush.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GenericPush  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtID;

    private String p_ExtCategoryID;

    private String p_ExtLocationID;

    private String p_ServiceNameNCode;

    private String p_MoogaBasicServiceType;

    public Generic_GenericPush() {
    }

    public Generic_GenericPush(
           int p_Key,
           String p_ExtID,
           String p_ExtCategoryID,
           String p_ExtLocationID,
           String p_ServiceNameNCode,
           String p_MoogaBasicServiceType) {
           this.p_Key = p_Key;
           this.p_ExtID = p_ExtID;
           this.p_ExtCategoryID = p_ExtCategoryID;
           this.p_ExtLocationID = p_ExtLocationID;
           this.p_ServiceNameNCode = p_ServiceNameNCode;
           this.p_MoogaBasicServiceType = p_MoogaBasicServiceType;
    }


    /**
     * Gets the p_Key value for this Generic_GenericPush.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Generic_GenericPush.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtID value for this Generic_GenericPush.
     * 
     * @return p_ExtID
     */
    public String getP_ExtID() {
        return p_ExtID;
    }


    /**
     * Sets the p_ExtID value for this Generic_GenericPush.
     * 
     * @param p_ExtID
     */
    public void setP_ExtID(String p_ExtID) {
        this.p_ExtID = p_ExtID;
    }


    /**
     * Gets the p_ExtCategoryID value for this Generic_GenericPush.
     * 
     * @return p_ExtCategoryID
     */
    public String getP_ExtCategoryID() {
        return p_ExtCategoryID;
    }


    /**
     * Sets the p_ExtCategoryID value for this Generic_GenericPush.
     * 
     * @param p_ExtCategoryID
     */
    public void setP_ExtCategoryID(String p_ExtCategoryID) {
        this.p_ExtCategoryID = p_ExtCategoryID;
    }


    /**
     * Gets the p_ExtLocationID value for this Generic_GenericPush.
     * 
     * @return p_ExtLocationID
     */
    public String getP_ExtLocationID() {
        return p_ExtLocationID;
    }


    /**
     * Sets the p_ExtLocationID value for this Generic_GenericPush.
     * 
     * @param p_ExtLocationID
     */
    public void setP_ExtLocationID(String p_ExtLocationID) {
        this.p_ExtLocationID = p_ExtLocationID;
    }


    /**
     * Gets the p_ServiceNameNCode value for this Generic_GenericPush.
     * 
     * @return p_ServiceNameNCode
     */
    public String getP_ServiceNameNCode() {
        return p_ServiceNameNCode;
    }


    /**
     * Sets the p_ServiceNameNCode value for this Generic_GenericPush.
     * 
     * @param p_ServiceNameNCode
     */
    public void setP_ServiceNameNCode(String p_ServiceNameNCode) {
        this.p_ServiceNameNCode = p_ServiceNameNCode;
    }


    /**
     * Gets the p_MoogaBasicServiceType value for this Generic_GenericPush.
     * 
     * @return p_MoogaBasicServiceType
     */
    public String getP_MoogaBasicServiceType() {
        return p_MoogaBasicServiceType;
    }


    /**
     * Sets the p_MoogaBasicServiceType value for this Generic_GenericPush.
     * 
     * @param p_MoogaBasicServiceType
     */
    public void setP_MoogaBasicServiceType(String p_MoogaBasicServiceType) {
        this.p_MoogaBasicServiceType = p_MoogaBasicServiceType;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GenericPush)) return false;
        Generic_GenericPush other = (Generic_GenericPush) obj;
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
            ((this.p_ExtCategoryID==null && other.getP_ExtCategoryID()==null) || 
             (this.p_ExtCategoryID!=null &&
              this.p_ExtCategoryID.equals(other.getP_ExtCategoryID()))) &&
            ((this.p_ExtLocationID==null && other.getP_ExtLocationID()==null) || 
             (this.p_ExtLocationID!=null &&
              this.p_ExtLocationID.equals(other.getP_ExtLocationID()))) &&
            ((this.p_ServiceNameNCode==null && other.getP_ServiceNameNCode()==null) || 
             (this.p_ServiceNameNCode!=null &&
              this.p_ServiceNameNCode.equals(other.getP_ServiceNameNCode()))) &&
            ((this.p_MoogaBasicServiceType==null && other.getP_MoogaBasicServiceType()==null) || 
             (this.p_MoogaBasicServiceType!=null &&
              this.p_MoogaBasicServiceType.equals(other.getP_MoogaBasicServiceType())));
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
        if (getP_ExtCategoryID() != null) {
            _hashCode += getP_ExtCategoryID().hashCode();
        }
        if (getP_ExtLocationID() != null) {
            _hashCode += getP_ExtLocationID().hashCode();
        }
        if (getP_ServiceNameNCode() != null) {
            _hashCode += getP_ServiceNameNCode().hashCode();
        }
        if (getP_MoogaBasicServiceType() != null) {
            _hashCode += getP_MoogaBasicServiceType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GenericPush.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GenericPush"));
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
        elemField.setFieldName("p_ExtCategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtCategoryID"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceNameNCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceNameNCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_MoogaBasicServiceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_MoogaBasicServiceType"));
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
