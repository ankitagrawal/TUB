/**
 * Refresh_DBFiltersResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Refresh_DBFiltersResponse  implements java.io.Serializable {
    private String refresh_DBFiltersResult;

    public Refresh_DBFiltersResponse() {
    }

    public Refresh_DBFiltersResponse(
           String refresh_DBFiltersResult) {
           this.refresh_DBFiltersResult = refresh_DBFiltersResult;
    }


    /**
     * Gets the refresh_DBFiltersResult value for this Refresh_DBFiltersResponse.
     * 
     * @return refresh_DBFiltersResult
     */
    public String getRefresh_DBFiltersResult() {
        return refresh_DBFiltersResult;
    }


    /**
     * Sets the refresh_DBFiltersResult value for this Refresh_DBFiltersResponse.
     * 
     * @param refresh_DBFiltersResult
     */
    public void setRefresh_DBFiltersResult(String refresh_DBFiltersResult) {
        this.refresh_DBFiltersResult = refresh_DBFiltersResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Refresh_DBFiltersResponse)) return false;
        Refresh_DBFiltersResponse other = (Refresh_DBFiltersResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.refresh_DBFiltersResult==null && other.getRefresh_DBFiltersResult()==null) || 
             (this.refresh_DBFiltersResult!=null &&
              this.refresh_DBFiltersResult.equals(other.getRefresh_DBFiltersResult())));
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
        if (getRefresh_DBFiltersResult() != null) {
            _hashCode += getRefresh_DBFiltersResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Refresh_DBFiltersResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Refresh_DBFiltersResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refresh_DBFiltersResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Refresh_DBFiltersResult"));
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
