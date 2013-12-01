/**
 * ReplaceIDsWithExtIDs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ReplaceIDsWithExtIDs  implements java.io.Serializable {
    private int p_Key;

    private String p_StrWithInternalIDs;

    private String p_MoogaIDType;

    private String p_MoogaCatIDType;

    private String p_ValueSeparator;

    private String p_PercSeparator;

    public ReplaceIDsWithExtIDs() {
    }

    public ReplaceIDsWithExtIDs(
           int p_Key,
           String p_StrWithInternalIDs,
           String p_MoogaIDType,
           String p_MoogaCatIDType,
           String p_ValueSeparator,
           String p_PercSeparator) {
           this.p_Key = p_Key;
           this.p_StrWithInternalIDs = p_StrWithInternalIDs;
           this.p_MoogaIDType = p_MoogaIDType;
           this.p_MoogaCatIDType = p_MoogaCatIDType;
           this.p_ValueSeparator = p_ValueSeparator;
           this.p_PercSeparator = p_PercSeparator;
    }


    /**
     * Gets the p_Key value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_StrWithInternalIDs value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_StrWithInternalIDs
     */
    public String getP_StrWithInternalIDs() {
        return p_StrWithInternalIDs;
    }


    /**
     * Sets the p_StrWithInternalIDs value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_StrWithInternalIDs
     */
    public void setP_StrWithInternalIDs(String p_StrWithInternalIDs) {
        this.p_StrWithInternalIDs = p_StrWithInternalIDs;
    }


    /**
     * Gets the p_MoogaIDType value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_MoogaIDType
     */
    public String getP_MoogaIDType() {
        return p_MoogaIDType;
    }


    /**
     * Sets the p_MoogaIDType value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_MoogaIDType
     */
    public void setP_MoogaIDType(String p_MoogaIDType) {
        this.p_MoogaIDType = p_MoogaIDType;
    }


    /**
     * Gets the p_MoogaCatIDType value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_MoogaCatIDType
     */
    public String getP_MoogaCatIDType() {
        return p_MoogaCatIDType;
    }


    /**
     * Sets the p_MoogaCatIDType value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_MoogaCatIDType
     */
    public void setP_MoogaCatIDType(String p_MoogaCatIDType) {
        this.p_MoogaCatIDType = p_MoogaCatIDType;
    }


    /**
     * Gets the p_ValueSeparator value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_ValueSeparator
     */
    public String getP_ValueSeparator() {
        return p_ValueSeparator;
    }


    /**
     * Sets the p_ValueSeparator value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_ValueSeparator
     */
    public void setP_ValueSeparator(String p_ValueSeparator) {
        this.p_ValueSeparator = p_ValueSeparator;
    }


    /**
     * Gets the p_PercSeparator value for this ReplaceIDsWithExtIDs.
     * 
     * @return p_PercSeparator
     */
    public String getP_PercSeparator() {
        return p_PercSeparator;
    }


    /**
     * Sets the p_PercSeparator value for this ReplaceIDsWithExtIDs.
     * 
     * @param p_PercSeparator
     */
    public void setP_PercSeparator(String p_PercSeparator) {
        this.p_PercSeparator = p_PercSeparator;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ReplaceIDsWithExtIDs)) return false;
        ReplaceIDsWithExtIDs other = (ReplaceIDsWithExtIDs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_StrWithInternalIDs==null && other.getP_StrWithInternalIDs()==null) || 
             (this.p_StrWithInternalIDs!=null &&
              this.p_StrWithInternalIDs.equals(other.getP_StrWithInternalIDs()))) &&
            ((this.p_MoogaIDType==null && other.getP_MoogaIDType()==null) || 
             (this.p_MoogaIDType!=null &&
              this.p_MoogaIDType.equals(other.getP_MoogaIDType()))) &&
            ((this.p_MoogaCatIDType==null && other.getP_MoogaCatIDType()==null) || 
             (this.p_MoogaCatIDType!=null &&
              this.p_MoogaCatIDType.equals(other.getP_MoogaCatIDType()))) &&
            ((this.p_ValueSeparator==null && other.getP_ValueSeparator()==null) || 
             (this.p_ValueSeparator!=null &&
              this.p_ValueSeparator.equals(other.getP_ValueSeparator()))) &&
            ((this.p_PercSeparator==null && other.getP_PercSeparator()==null) || 
             (this.p_PercSeparator!=null &&
              this.p_PercSeparator.equals(other.getP_PercSeparator())));
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
        if (getP_StrWithInternalIDs() != null) {
            _hashCode += getP_StrWithInternalIDs().hashCode();
        }
        if (getP_MoogaIDType() != null) {
            _hashCode += getP_MoogaIDType().hashCode();
        }
        if (getP_MoogaCatIDType() != null) {
            _hashCode += getP_MoogaCatIDType().hashCode();
        }
        if (getP_ValueSeparator() != null) {
            _hashCode += getP_ValueSeparator().hashCode();
        }
        if (getP_PercSeparator() != null) {
            _hashCode += getP_PercSeparator().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReplaceIDsWithExtIDs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ReplaceIDsWithExtIDs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_StrWithInternalIDs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_StrWithInternalIDs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_MoogaIDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_MoogaIDType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_MoogaCatIDType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_MoogaCatIDType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ValueSeparator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ValueSeparator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_PercSeparator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_PercSeparator"));
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
