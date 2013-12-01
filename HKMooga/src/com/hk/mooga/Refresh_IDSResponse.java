/**
 * Refresh_IDSResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Refresh_IDSResponse  implements java.io.Serializable {
    private String refresh_IDSResult;

    public Refresh_IDSResponse() {
    }

    public Refresh_IDSResponse(
           String refresh_IDSResult) {
           this.refresh_IDSResult = refresh_IDSResult;
    }


    /**
     * Gets the refresh_IDSResult value for this Refresh_IDSResponse.
     * 
     * @return refresh_IDSResult
     */
    public String getRefresh_IDSResult() {
        return refresh_IDSResult;
    }


    /**
     * Sets the refresh_IDSResult value for this Refresh_IDSResponse.
     * 
     * @param refresh_IDSResult
     */
    public void setRefresh_IDSResult(String refresh_IDSResult) {
        this.refresh_IDSResult = refresh_IDSResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Refresh_IDSResponse)) return false;
        Refresh_IDSResponse other = (Refresh_IDSResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.refresh_IDSResult==null && other.getRefresh_IDSResult()==null) || 
             (this.refresh_IDSResult!=null &&
              this.refresh_IDSResult.equals(other.getRefresh_IDSResult())));
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
        if (getRefresh_IDSResult() != null) {
            _hashCode += getRefresh_IDSResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Refresh_IDSResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Refresh_IDSResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refresh_IDSResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Refresh_IDSResult"));
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
