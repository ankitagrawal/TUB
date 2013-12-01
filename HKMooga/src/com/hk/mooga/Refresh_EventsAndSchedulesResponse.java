/**
 * Refresh_EventsAndSchedulesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Refresh_EventsAndSchedulesResponse  implements java.io.Serializable {
    private String refresh_EventsAndSchedulesResult;

    public Refresh_EventsAndSchedulesResponse() {
    }

    public Refresh_EventsAndSchedulesResponse(
           String refresh_EventsAndSchedulesResult) {
           this.refresh_EventsAndSchedulesResult = refresh_EventsAndSchedulesResult;
    }


    /**
     * Gets the refresh_EventsAndSchedulesResult value for this Refresh_EventsAndSchedulesResponse.
     * 
     * @return refresh_EventsAndSchedulesResult
     */
    public String getRefresh_EventsAndSchedulesResult() {
        return refresh_EventsAndSchedulesResult;
    }


    /**
     * Sets the refresh_EventsAndSchedulesResult value for this Refresh_EventsAndSchedulesResponse.
     * 
     * @param refresh_EventsAndSchedulesResult
     */
    public void setRefresh_EventsAndSchedulesResult(String refresh_EventsAndSchedulesResult) {
        this.refresh_EventsAndSchedulesResult = refresh_EventsAndSchedulesResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Refresh_EventsAndSchedulesResponse)) return false;
        Refresh_EventsAndSchedulesResponse other = (Refresh_EventsAndSchedulesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.refresh_EventsAndSchedulesResult==null && other.getRefresh_EventsAndSchedulesResult()==null) || 
             (this.refresh_EventsAndSchedulesResult!=null &&
              this.refresh_EventsAndSchedulesResult.equals(other.getRefresh_EventsAndSchedulesResult())));
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
        if (getRefresh_EventsAndSchedulesResult() != null) {
            _hashCode += getRefresh_EventsAndSchedulesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Refresh_EventsAndSchedulesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Refresh_EventsAndSchedulesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refresh_EventsAndSchedulesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Refresh_EventsAndSchedulesResult"));
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
