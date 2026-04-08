/**
 * WSDSSaleOrderWSFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.javaboot.webservice.order;

public class WSDSSaleOrderWSFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSrvProxyService {

    public WSDSSaleOrderWSFacadeSrvProxyServiceLocator() {
    }


    public WSDSSaleOrderWSFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSDSSaleOrderWSFacadeSrvProxyServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSDSSaleOrderWSFacade
    private java.lang.String WSDSSaleOrderWSFacade_address = "http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSSaleOrderWSFacade";

    public java.lang.String getWSDSSaleOrderWSFacadeAddress() {
        return WSDSSaleOrderWSFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSDSSaleOrderWSFacadeWSDDServiceName = "WSDSSaleOrderWSFacade";

    public java.lang.String getWSDSSaleOrderWSFacadeWSDDServiceName() {
        return WSDSSaleOrderWSFacadeWSDDServiceName;
    }

    public void setWSDSSaleOrderWSFacadeWSDDServiceName(java.lang.String name) {
        WSDSSaleOrderWSFacadeWSDDServiceName = name;
    }

    public com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSrvProxy getWSDSSaleOrderWSFacade() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSDSSaleOrderWSFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSDSSaleOrderWSFacade(endpoint);
    }

    public com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSrvProxy getWSDSSaleOrderWSFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSDSSaleOrderWSFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSDSSaleOrderWSFacadeEndpointAddress(java.lang.String address) {
        WSDSSaleOrderWSFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSoapBindingStub _stub = new com.javaboot.webservice.order.WSDSSaleOrderWSFacadeSoapBindingStub(new java.net.URL(WSDSSaleOrderWSFacade_address), this);
                _stub.setPortName(getWSDSSaleOrderWSFacadeWSDDServiceName());
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
        if ("WSDSSaleOrderWSFacade".equals(inputPortName)) {
            return getWSDSSaleOrderWSFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSSaleOrderWSFacade", "WSDSSaleOrderWSFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://erp.xiangjiamuye.com:6892/ormrpc/services/WSDSSaleOrderWSFacade", "WSDSSaleOrderWSFacade"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("WSDSSaleOrderWSFacade".equals(portName)) {
            setWSDSSaleOrderWSFacadeEndpointAddress(address);
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
