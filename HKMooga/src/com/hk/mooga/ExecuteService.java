/**
 * ExecuteService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteService  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtPartyID;

    private String p_ExtID;

    private String p_ExtCategoryID;

    private String p_ExtLocationID;

    private String p_ExtTimeID;

    private short p_ServiceID;

    public ExecuteService() {
    }

    public ExecuteService(
           int p_Key,
           String p_ExtPartyID,
           String p_ExtID,
           String p_ExtCategoryID,
           String p_ExtLocationID,
           String p_ExtTimeID,
           short p_ServiceID) {
           this.p_Key = p_Key;
           this.p_ExtPartyID = p_ExtPartyID;
           this.p_ExtID = p_ExtID;
           this.p_ExtCategoryID = p_ExtCategoryID;
           this.p_ExtLocationID = p_ExtLocationID;
           this.p_ExtTimeID = p_ExtTimeID;
           this.p_ServiceID = p_ServiceID;
    }


    /**
     * Gets the p_Key value for this ExecuteService.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this ExecuteService.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtPartyID value for this ExecuteService.
     * 
     * @return p_ExtPartyID
     */
    public String getP_ExtPartyID() {
        return p_ExtPartyID;
    }


    /**
     * Sets the p_ExtPartyID value for this ExecuteService.
     * 
     * @param p_ExtPartyID
     */
    public void setP_ExtPartyID(String p_ExtPartyID) {
        this.p_ExtPartyID = p_ExtPartyID;
    }


    /**
     * Gets the p_ExtID value for this ExecuteService.
     * 
     * @return p_ExtID
     */
    public String getP_ExtID() {
        return p_ExtID;
    }


    /**
     * Sets the p_ExtID value for this ExecuteService.
     * 
     * @param p_ExtID
     */
    public void setP_ExtID(String p_ExtID) {
        this.p_ExtID = p_ExtID;
    }


    /**
     * Gets the p_ExtCategoryID value for this ExecuteService.
     * 
     * @return p_ExtCategoryID
     */
    public String getP_ExtCategoryID() {
        return p_ExtCategoryID;
    }


    /**
     * Sets the p_ExtCategoryID value for this ExecuteService.
     * 
     * @param p_ExtCategoryID
     */
    public void setP_ExtCategoryID(String p_ExtCategoryID) {
        this.p_ExtCategoryID = p_ExtCategoryID;
    }


    /**
     * Gets the p_ExtLocationID value for this ExecuteService.
     * 
     * @return p_ExtLocationID
     */
    public String getP_ExtLocationID() {
        return p_ExtLocationID;
    }


    /**
     * Sets the p_ExtLocationID value for this ExecuteService.
     * 
     * @param p_ExtLocationID
     */
    public void setP_ExtLocationID(String p_ExtLocationID) {
        this.p_ExtLocationID = p_ExtLocationID;
    }


    /**
     * Gets the p_ExtTimeID value for this ExecuteService.
     * 
     * @return p_ExtTimeID
     */
    public String getP_ExtTimeID() {
        return p_ExtTimeID;
    }


    /**
     * Sets the p_ExtTimeID value for this ExecuteService.
     * 
     * @param p_ExtTimeID
     */
    public void setP_ExtTimeID(String p_ExtTimeID) {
        this.p_ExtTimeID = p_ExtTimeID;
    }


    /**
     * Gets the p_ServiceID value for this ExecuteService.
     * 
     * @return p_ServiceID
     */
    public short getP_ServiceID() {
        return p_ServiceID;
    }


    /**
     * Sets the p_ServiceID value for this ExecuteService.
     * 
     * @param p_ServiceID
     */
    public void setP_ServiceID(short p_ServiceID) {
        this.p_ServiceID = p_ServiceID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteService)) return false;
        ExecuteService other = (ExecuteService) obj;
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
            ((this.p_ExtID==null && other.getP_ExtID()==null) || 
             (this.p_ExtID!=null &&
              this.p_ExtID.equals(other.getP_ExtID()))) &&
            ((this.p_ExtCategoryID==null && other.getP_ExtCategoryID()==null) || 
             (this.p_ExtCategoryID!=null &&
              this.p_ExtCategoryID.equals(other.getP_ExtCategoryID()))) &&
            ((this.p_ExtLocationID==null && other.getP_ExtLocationID()==null) || 
             (this.p_ExtLocationID!=null &&
              this.p_ExtLocationID.equals(other.getP_ExtLocationID()))) &&
            ((this.p_ExtTimeID==null && other.getP_ExtTimeID()==null) || 
             (this.p_ExtTimeID!=null &&
              this.p_ExtTimeID.equals(other.getP_ExtTimeID()))) &&
            this.p_ServiceID == other.getP_ServiceID();
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
        if (getP_ExtID() != null) {
            _hashCode += getP_ExtID().hashCode();
        }
        if (getP_ExtCategoryID() != null) {
            _hashCode += getP_ExtCategoryID().hashCode();
        }
        if (getP_ExtLocationID() != null) {
            _hashCode += getP_ExtLocationID().hashCode();
        }
        if (getP_ExtTimeID() != null) {
            _hashCode += getP_ExtTimeID().hashCode();
        }
        _hashCode += getP_ServiceID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteService.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteService"));
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
        elemField.setFieldName("p_ExtTimeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtTimeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServiceID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServiceID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
