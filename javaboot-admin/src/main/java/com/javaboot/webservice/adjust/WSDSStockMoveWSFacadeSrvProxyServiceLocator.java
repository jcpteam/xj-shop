/**
 * WSDSStockMoveWSFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.javaboot.webservice.adjust;

public class WSDSStockMoveWSFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSrvProxyService {

    public WSDSStockMoveWSFacadeSrvProxyServiceLocator() {
    }


    public WSDSStockMoveWSFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSDSStockMoveWSFacadeSrvProxyServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSDSStockMoveWSFacade
    private java.lang.String WSDSStockMoveWSFacade_address = "http://erp.xiangjiamuye.com:6888/ormrpc/services/WSDSStockMoveWSFacade";

    public java.lang.String getWSDSStockMoveWSFacadeAddress() {
        return WSDSStockMoveWSFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSDSStockMoveWSFacadeWSDDServiceName = "WSDSStockMoveWSFacade";

    public java.lang.String getWSDSStockMoveWSFacadeWSDDServiceName() {
        return WSDSStockMoveWSFacadeWSDDServiceName;
    }

    public void setWSDSStockMoveWSFacadeWSDDServiceName(java.lang.String name) {
        WSDSStockMoveWSFacadeWSDDServiceName = name;
    }

    public com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSrvProxy getWSDSStockMoveWSFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSDSStockMoveWSFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSDSStockMoveWSFacade(endpoint);
    }

    public com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSrvProxy getWSDSStockMoveWSFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSDSStockMoveWSFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSDSStockMoveWSFacadeEndpointAddress(java.lang.String address) {
        WSDSStockMoveWSFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.adjust.WSDSStockMoveWSFacadeSoapBindingStub(new java.net.URL(WSDSStockMoveWSFacade_address), this);
                _stub.setPortName(getWSDSStockMoveWSFacadeWSDDServiceName());
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
        if ("WSDSStockMoveWSFacade".equals(inputPortName)) {
            return getWSDSStockMoveWSFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6888/ormrpc/services/WSDSStockMoveWSFacade", "WSDSStockMoveWSFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6888/ormrpc/services/WSDSStockMoveWSFacade", "WSDSStockMoveWSFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSDSStockMoveWSFacade".equals(portName)) {
            setWSDSStockMoveWSFacadeEndpointAddress(address);
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
