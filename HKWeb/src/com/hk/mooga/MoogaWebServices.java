/**
 * MoogaWebServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public interface MoogaWebServices extends javax.xml.rpc.Service {
    public java.lang.String getMoogaWebServicesSoap12Address();

    public com.hk.mooga.MoogaWebServicesSoap_PortType getMoogaWebServicesSoap12() throws javax.xml.rpc.ServiceException;

    public com.hk.mooga.MoogaWebServicesSoap_PortType getMoogaWebServicesSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getMoogaWebServicesSoapAddress();

    public com.hk.mooga.MoogaWebServicesSoap_PortType getMoogaWebServicesSoap() throws javax.xml.rpc.ServiceException;

    public com.hk.mooga.MoogaWebServicesSoap_PortType getMoogaWebServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
