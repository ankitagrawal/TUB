/**
 * Party_GeneratePendingPersonalisedItemsToConsumer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Party_GeneratePendingPersonalisedItemsToConsumer  implements java.io.Serializable {
    private int p_Key;

    private String p_ExtItemCategoryID;

    private String p_ExtPartyLocationID;

    private String p_ServiceCode;

    private int p_ServerID;

    public Party_GeneratePendingPersonalisedItemsToConsumer() {
    }

    public Party_GeneratePendingPersonalisedItemsToConsumer(
           int p_Key,
           String p_ExtItemCategoryID,
           String p_ExtPartyLocationID,
           String p_ServiceCode,
           int p_ServerID) {
           this.p_Key = p_Key;
           this.p_ExtItemCategoryID = p_ExtItemCategoryID;
           this.p_ExtPartyLocationID = p_ExtPartyLocationID;
           this.p_ServiceCode = p_ServiceCode;
           this.p_ServerID = p_ServerID;
    }


    /**
     * Gets the p_Key value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @return p_Key
     */
    public int getP_Key() {
        return p_Key;
    }


    /**
     * Sets the p_Key value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @param p_Key
     */
    public void setP_Key(int p_Key) {
        this.p_Key = p_Key;
    }


    /**
     * Gets the p_ExtItemCategoryID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @return p_ExtItemCategoryID
     */
    public String getP_ExtItemCategoryID() {
        return p_ExtItemCategoryID;
    }


    /**
     * Sets the p_ExtItemCategoryID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @param p_ExtItemCategoryID
     */
    public void setP_ExtItemCategoryID(String p_ExtItemCategoryID) {
        this.p_ExtItemCategoryID = p_ExtItemCategoryID;
    }


    /**
     * Gets the p_ExtPartyLocationID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @return p_ExtPartyLocationID
     */
    public String getP_ExtPartyLocationID() {
        return p_ExtPartyLocationID;
    }


    /**
     * Sets the p_ExtPartyLocationID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @param p_ExtPartyLocationID
     */
    public void setP_ExtPartyLocationID(String p_ExtPartyLocationID) {
        this.p_ExtPartyLocationID = p_ExtPartyLocationID;
    }


    /**
     * Gets the p_ServiceCode value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @return p_ServiceCode
     */
    public String getP_ServiceCode() {
        return p_ServiceCode;
    }


    /**
     * Sets the p_ServiceCode value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @param p_ServiceCode
     */
    public void setP_ServiceCode(String p_ServiceCode) {
        this.p_ServiceCode = p_ServiceCode;
    }


    /**
     * Gets the p_ServerID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @return p_ServerID
     */
    public int getP_ServerID() {
        return p_ServerID;
    }


    /**
     * Sets the p_ServerID value for this Party_GeneratePendingPersonalisedItemsToConsumer.
     * 
     * @param p_ServerID
     */
    public void setP_ServerID(int p_ServerID) {
        this.p_ServerID = p_ServerID;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Party_GeneratePendingPersonalisedItemsToConsumer)) return false;
        Party_GeneratePendingPersonalisedItemsToConsumer other = (Party_GeneratePendingPersonalisedItemsToConsumer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.p_Key == other.getP_Key() &&
            ((this.p_ExtItemCategoryID==null && other.getP_ExtItemCategoryID()==null) || 
             (this.p_ExtItemCategoryID!=null &&
              this.p_ExtItemCategoryID.equals(other.getP_ExtItemCategoryID()))) &&
            ((this.p_ExtPartyLocationID==null && other.getP_ExtPartyLocationID()==null) || 
             (this.p_ExtPartyLocationID!=null &&
              this.p_ExtPartyLocationID.equals(other.getP_ExtPartyLocationID()))) &&
            ((this.p_ServiceCode==null && other.getP_ServiceCode()==null) || 
             (this.p_ServiceCode!=null &&
              this.p_ServiceCode.equals(other.getP_ServiceCode()))) &&
            this.p_ServerID == other.getP_ServerID();
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
        if (getP_ExtItemCategoryID() != null) {
            _hashCode += getP_ExtItemCategoryID().hashCode();
        }
        if (getP_ExtPartyLocationID() != null) {
            _hashCode += getP_ExtPartyLocationID().hashCode();
        }
        if (getP_ServiceCode() != null) {
            _hashCode += getP_ServiceCode().hashCode();
        }
        _hashCode += getP_ServerID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Party_GeneratePendingPersonalisedItemsToConsumer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Party_GeneratePendingPersonalisedItemsToConsumer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("p_ExtPartyLocationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ExtPartyLocationID"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_ServerID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "p_ServerID"));
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
