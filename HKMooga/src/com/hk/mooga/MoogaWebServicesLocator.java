/**
 * MoogaWebServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public class MoogaWebServicesLocator extends org.apache.axis.client.Service implements MoogaWebServices {

    public MoogaWebServicesLocator() {
    }


    public MoogaWebServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MoogaWebServicesLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MoogaWebServicesSoap12
    private String MoogaWebServicesSoap12_address = "http://110.234.176.23/MoogaIntegration/MoogaWebservices.asmx";

    public String getMoogaWebServicesSoap12Address() {
        return MoogaWebServicesSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private String MoogaWebServicesSoap12WSDDServiceName = "MoogaWebServicesSoap12";

    public String getMoogaWebServicesSoap12WSDDServiceName() {
        return MoogaWebServicesSoap12WSDDServiceName;
    }

    public void setMoogaWebServicesSoap12WSDDServiceName(String name) {
        MoogaWebServicesSoap12WSDDServiceName = name;
    }

    public MoogaWebServicesSoap_PortType getMoogaWebServicesSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MoogaWebServicesSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMoogaWebServicesSoap12(endpoint);
    }

    public MoogaWebServicesSoap_PortType getMoogaWebServicesSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            MoogaWebServicesSoap12Stub _stub = new MoogaWebServicesSoap12Stub(portAddress, this);
            _stub.setPortName(getMoogaWebServicesSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMoogaWebServicesSoap12EndpointAddress(String address) {
        MoogaWebServicesSoap12_address = address;
    }


    // Use to get a proxy class for MoogaWebServicesSoap
    private String MoogaWebServicesSoap_address = "http://110.234.176.23/HealthKart_Mooga/moogawebservices.asmx";

    public String getMoogaWebServicesSoapAddress() {
        return MoogaWebServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String MoogaWebServicesSoapWSDDServiceName = "MoogaWebServicesSoap";

    public String getMoogaWebServicesSoapWSDDServiceName() {
        return MoogaWebServicesSoapWSDDServiceName;
    }

    public void setMoogaWebServicesSoapWSDDServiceName(String name) {
        MoogaWebServicesSoapWSDDServiceName = name;
    }

    public MoogaWebServicesSoap_PortType getMoogaWebServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MoogaWebServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMoogaWebServicesSoap(endpoint);
    }

    public MoogaWebServicesSoap_PortType getMoogaWebServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            MoogaWebServicesSoap_BindingStub _stub = new MoogaWebServicesSoap_BindingStub(portAddress, this);
            _stub.setPortName(getMoogaWebServicesSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMoogaWebServicesSoapEndpointAddress(String address) {
        MoogaWebServicesSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (MoogaWebServicesSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                MoogaWebServicesSoap12Stub _stub = new MoogaWebServicesSoap12Stub(new java.net.URL(MoogaWebServicesSoap12_address), this);
                _stub.setPortName(getMoogaWebServicesSoap12WSDDServiceName());
                return _stub;
            }
            if (MoogaWebServicesSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                MoogaWebServicesSoap_BindingStub _stub = new MoogaWebServicesSoap_BindingStub(new java.net.URL(MoogaWebServicesSoap_address), this);
                _stub.setPortName(getMoogaWebServicesSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("MoogaWebServicesSoap12".equals(inputPortName)) {
            return getMoogaWebServicesSoap12();
        }
        else if ("MoogaWebServicesSoap".equals(inputPortName)) {
            return getMoogaWebServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "MoogaWebServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "MoogaWebServicesSoap12"));
            ports.add(new javax.xml.namespace.QName("http://ikensolution.com/webservices/", "MoogaWebServicesSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("MoogaWebServicesSoap12".equals(portName)) {
            setMoogaWebServicesSoap12EndpointAddress(address);
        }
        else 
if ("MoogaWebServicesSoap".equals(portName)) {
            setMoogaWebServicesSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
