/**
 * Trans_NotifyTransUsingListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class Trans_NotifyTransUsingListResponse  implements java.io.Serializable {
    private String trans_NotifyTransUsingListResult;

    public Trans_NotifyTransUsingListResponse() {
    }

    public Trans_NotifyTransUsingListResponse(
           String trans_NotifyTransUsingListResult) {
           this.trans_NotifyTransUsingListResult = trans_NotifyTransUsingListResult;
    }


    /**
     * Gets the trans_NotifyTransUsingListResult value for this Trans_NotifyTransUsingListResponse.
     * 
     * @return trans_NotifyTransUsingListResult
     */
    public String getTrans_NotifyTransUsingListResult() {
        return trans_NotifyTransUsingListResult;
    }


    /**
     * Sets the trans_NotifyTransUsingListResult value for this Trans_NotifyTransUsingListResponse.
     * 
     * @param trans_NotifyTransUsingListResult
     */
    public void setTrans_NotifyTransUsingListResult(String trans_NotifyTransUsingListResult) {
        this.trans_NotifyTransUsingListResult = trans_NotifyTransUsingListResult;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Trans_NotifyTransUsingListResponse)) return false;
        Trans_NotifyTransUsingListResponse other = (Trans_NotifyTransUsingListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.trans_NotifyTransUsingListResult==null && other.getTrans_NotifyTransUsingListResult()==null) || 
             (this.trans_NotifyTransUsingListResult!=null &&
              this.trans_NotifyTransUsingListResult.equals(other.getTrans_NotifyTransUsingListResult())));
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
        if (getTrans_NotifyTransUsingListResult() != null) {
            _hashCode += getTrans_NotifyTransUsingListResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Trans_NotifyTransUsingListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", ">Trans_NotifyTransUsingListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trans_NotifyTransUsingListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "Trans_NotifyTransUsingListResult"));
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
