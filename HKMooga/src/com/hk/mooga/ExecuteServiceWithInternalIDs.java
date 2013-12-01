/**
 * ExecuteServiceWithInternalIDs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class ExecuteServiceWithInternalIDs  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtPartyID;

    private String p_ID;

    private String p_CategoryID;

    private String p_LocationID;

    private String p_TimeID;

    private short p_ServiceID;

    public ExecuteServiceWithInternalIDs() {
    }

    public ExecuteServiceWithInternalIDs(
           int p_Key,
           String p_ExtPartyID,
           String p_ID,
           String p_CategoryID,
           String p_LocationID,
           String p_TimeID,
           short p_ServiceID) {
           this.p_Key = p_Key;
           this.p_ExtPartyID = p_ExtPartyID;
           this.p_ID = p_ID;
           this.p_CategoryID = p_CategoryID;
           this.p_LocationID = p_LocationID;
           this.p_TimeID = p_TimeID;
           this.p_ServiceID = p_ServiceID;
    }


    /**
     * Gets the p_Key value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtPartyID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_ExtPartyID
     */
    public String getP_ExtPartyID() {
        return p_ExtPartyID;
    }


    /**
     * Sets the p_ExtPartyID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_ExtPartyID
     */
    public void setP_ExtPartyID(String p_ExtPartyID) {
        this.p_ExtPartyID = p_ExtPartyID;
    }


    /**
     * Gets the p_ID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_ID
     */
    public String getP_ID() {
        return p_ID;
    }


    /**
     * Sets the p_ID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_ID
     */
    public void setP_ID(String p_ID) {
        this.p_ID = p_ID;
    }


    /**
     * Gets the p_CategoryID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_CategoryID
     */
    public String getP_CategoryID() {
        return p_CategoryID;
    }


    /**
     * Sets the p_CategoryID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_CategoryID
     */
    public void setP_CategoryID(String p_CategoryID) {
        this.p_CategoryID = p_CategoryID;
    }


    /**
     * Gets the p_LocationID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_LocationID
     */
    public String getP_LocationID() {
        return p_LocationID;
    }


    /**
     * Sets the p_LocationID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_LocationID
     */
    public void setP_LocationID(String p_LocationID) {
        this.p_LocationID = p_LocationID;
    }


    /**
     * Gets the p_TimeID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_TimeID
     */
    public String getP_TimeID() {
        return p_TimeID;
    }


    /**
     * Sets the p_TimeID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_TimeID
     */
    public void setP_TimeID(String p_TimeID) {
        this.p_TimeID = p_TimeID;
    }


    /**
     * Gets the p_ServiceID value for this ExecuteServiceWithInternalIDs.
     * 
     * @return p_ServiceID
     */
    public short getP_ServiceID() {
        return p_ServiceID;
    }


    /**
     * Sets the p_ServiceID value for this ExecuteServiceWithInternalIDs.
     * 
     * @param p_ServiceID
     */
    public void setP_ServiceID(short p_ServiceID) {
        this.p_ServiceID = p_ServiceID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ExecuteServiceWithInternalIDs)) return false;
        ExecuteServiceWithInternalIDs other = (ExecuteServiceWithInternalIDs) obj;
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
            ((this.p_ID==null && other.getP_ID()==null) || 
             (this.p_ID!=null &&
              this.p_ID.equals(other.getP_ID()))) &&
            ((this.p_CategoryID==null && other.getP_CategoryID()==null) || 
             (this.p_CategoryID!=null &&
              this.p_CategoryID.equals(other.getP_CategoryID()))) &&
            ((this.p_LocationID==null && other.getP_LocationID()==null) || 
             (this.p_LocationID!=null &&
              this.p_LocationID.equals(other.getP_LocationID()))) &&
            ((this.p_TimeID==null && other.getP_TimeID()==null) || 
             (this.p_TimeID!=null &&
              this.p_TimeID.equals(other.getP_TimeID()))) &&
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
        if (getP_ID() != null) {
            _hashCode += getP_ID().hashCode();
        }
        if (getP_CategoryID() != null) {
            _hashCode += getP_CategoryID().hashCode();
        }
        if (getP_LocationID() != null) {
            _hashCode += getP_LocationID().hashCode();
        }
        if (getP_TimeID() != null) {
            _hashCode += getP_TimeID().hashCode();
        }
        _hashCode += getP_ServiceID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteServiceWithInternalIDs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">ExecuteServiceWithInternalIDs"));
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
        elemField.setFieldName("p_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_CategoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_CategoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_LocationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_LocationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_TimeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_TimeID"));
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
