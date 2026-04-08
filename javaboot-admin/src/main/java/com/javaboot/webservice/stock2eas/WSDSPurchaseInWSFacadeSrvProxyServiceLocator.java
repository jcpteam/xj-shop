/**
 * WSDSPurchaseInWSFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.javaboot.webservice.stock2eas;

public class WSDSPurchaseInWSFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSrvProxyService {

    public WSDSPurchaseInWSFacadeSrvProxyServiceLocator() {
    }


    public WSDSPurchaseInWSFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSDSPurchaseInWSFacadeSrvProxyServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSDSPurchaseInWSFacade
    private java.lang.String WSDSPurchaseInWSFacade_address = "http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSPurchaseInWSFacade";

    public java.lang.String getWSDSPurchaseInWSFacadeAddress() {
        return WSDSPurchaseInWSFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSDSPurchaseInWSFacadeWSDDServiceName = "WSDSPurchaseInWSFacade";

    public java.lang.String getWSDSPurchaseInWSFacadeWSDDServiceName() {
        return WSDSPurchaseInWSFacadeWSDDServiceName;
    }

    public void setWSDSPurchaseInWSFacadeWSDDServiceName(java.lang.String name) {
        WSDSPurchaseInWSFacadeWSDDServiceName = name;
    }

    public com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSrvProxy getWSDSPurchaseInWSFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSDSPurchaseInWSFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSDSPurchaseInWSFacade(endpoint);
    }

    public com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSrvProxy getWSDSPurchaseInWSFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSDSPurchaseInWSFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSDSPurchaseInWSFacadeEndpointAddress(java.lang.String address) {
        WSDSPurchaseInWSFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.stock2eas.WSDSPurchaseInWSFacadeSoapBindingStub(new java.net.URL(WSDSPurchaseInWSFacade_address), this);
                _stub.setPortName(getWSDSPurchaseInWSFacadeWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSDSPurchaseInWSFacade".equals(inputPortName)) {
            return getWSDSPurchaseInWSFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSPurchaseInWSFacade", "WSDSPurchaseInWSFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSPurchaseInWSFacade", "WSDSPurchaseInWSFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSDSPurchaseInWSFacade".equals(portName)) {
            setWSDSPurchaseInWSFacadeEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
