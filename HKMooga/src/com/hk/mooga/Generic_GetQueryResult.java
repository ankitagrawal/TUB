/**
 * Generic_GetQueryResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Generic_GetQueryResult  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtItemOrPartyID;

    private String p_ExtItemCategoryID;

    private String p_ExtLocationID;

    private String p_ServiceNameNCode;

    private boolean p_IsConvertToExternalID;

    public Generic_GetQueryResult() {
    }

    public Generic_GetQueryResult(
           int p_Key,
           String p_ExtItemOrPartyID,
           String p_ExtItemCategoryID,
           String p_ExtLocationID,
           String p_ServiceNameNCode,
           boolean p_IsConvertToExternalID) {
           this.p_Key = p_Key;
           this.p_ExtItemOrPartyID = p_ExtItemOrPartyID;
           this.p_ExtItemCategoryID = p_ExtItemCategoryID;
           this.p_ExtLocationID = p_ExtLocationID;
           this.p_ServiceNameNCode = p_ServiceNameNCode;
           this.p_IsConvertToExternalID = p_IsConvertToExternalID;
    }


    /**
     * Gets the p_Key value for this Generic_GetQueryResult.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Generic_GetQueryResult.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtItemOrPartyID value for this Generic_GetQueryResult.
     * 
     * @return p_ExtItemOrPartyID
     */
    public String getP_ExtItemOrPartyID() {
        return p_ExtItemOrPartyID;
    }


    /**
     * Sets the p_ExtItemOrPartyID value for this Generic_GetQueryResult.
     * 
     * @param p_ExtItemOrPartyID
     */
    public void setP_ExtItemOrPartyID(String p_ExtItemOrPartyID) {
        this.p_ExtItemOrPartyID = p_ExtItemOrPartyID;
    }


    /**
     * Gets the p_ExtItemCategoryID value for this Generic_GetQueryResult.
     * 
     * @return p_ExtItemCategoryID
     */
    public String getP_ExtItemCategoryID() {
        return p_ExtItemCategoryID;
    }


    /**
     * Sets the p_ExtItemCategoryID value for this Generic_GetQueryResult.
     * 
     * @param p_ExtItemCategoryID
     */
    public void setP_ExtItemCategoryID(String p_ExtItemCategoryID) {
        this.p_ExtItemCategoryID = p_ExtItemCategoryID;
    }


    /**
     * Gets the p_ExtLocationID value for this Generic_GetQueryResult.
     * 
     * @return p_ExtLocationID
     */
    public String getP_ExtLocationID() {
        return p_ExtLocationID;
    }


    /**
     * Sets the p_ExtLocationID value for this Generic_GetQueryResult.
     * 
     * @param p_ExtLocationID
     */
    public void setP_ExtLocationID(String p_ExtLocationID) {
        this.p_ExtLocationID = p_ExtLocationID;
    }


    /**
     * Gets the p_ServiceNameNCode value for this Generic_GetQueryResult.
     * 
     * @return p_ServiceNameNCode
     */
    public String getP_ServiceNameNCode() {
        return p_ServiceNameNCode;
    }


    /**
     * Sets the p_ServiceNameNCode value for this Generic_GetQueryResult.
     * 
     * @param p_ServiceNameNCode
     */
    public void setP_ServiceNameNCode(String p_ServiceNameNCode) {
        this.p_ServiceNameNCode = p_ServiceNameNCode;
    }


    /**
     * Gets the p_IsConvertToExternalID value for this Generic_GetQueryResult.
     * 
     * @return p_IsConvertToExternalID
     */
    public boolean isP_IsConvertToExternalID() {
        return p_IsConvertToExternalID;
    }


    /**
     * Sets the p_IsConvertToExternalID value for this Generic_GetQueryResult.
     * 
     * @param p_IsConvertToExternalID
     */
    public void setP_IsConvertToExternalID(boolean p_IsConvertToExternalID) {
        this.p_IsConvertToExternalID = p_IsConvertToExternalID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Generic_GetQueryResult)) return false;
        Generic_GetQueryResult other = (Generic_GetQueryResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtItemOrPartyID==null && other.getP_ExtItemOrPartyID()==null) || 
             (this.p_ExtItemOrPartyID!=null &&
              this.p_ExtItemOrPartyID.equals(other.getP_ExtItemOrPartyID()))) &&
            ((this.p_ExtItemCategoryID==null && other.getP_ExtItemCategoryID()==null) || 
             (this.p_ExtItemCategoryID!=null &&
              this.p_ExtItemCategoryID.equals(other.getP_ExtItemCategoryID()))) &&
            ((this.p_ExtLocationID==null && other.getP_ExtLocationID()==null) || 
             (this.p_ExtLocationID!=null &&
              this.p_ExtLocationID.equals(other.getP_ExtLocationID()))) &&
            ((this.p_ServiceNameNCode==null && other.getP_ServiceNameNCode()==null) || 
             (this.p_ServiceNameNCode!=null &&
              this.p_ServiceNameNCode.equals(other.getP_ServiceNameNCode()))) &&
            this.p_IsConvertToExternalID == other.isP_IsConvertToExternalID();
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
        if (getP_ExtItemOrPartyID() != null) {
            _hashCode += getP_ExtItemOrPartyID().hashCode();
        }
        if (getP_ExtItemCategoryID() != null) {
            _hashCode += getP_ExtItemCategoryID().hashCode();
        }
        if (getP_ExtLocationID() != null) {
            _hashCode += getP_ExtLocationID().hashCode();
        }
        if (getP_ServiceNameNCode() != null) {
            _hashCode += getP_ServiceNameNCode().hashCode();
        }
        _hashCode += (isP_IsConvertToExternalID() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Generic_GetQueryResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Generic_GetQueryResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ExtItemOrPartyID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtItemOrPartyID"));
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
        elemField.setFieldName("p_IsConvertToExternalID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_IsConvertToExternalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
