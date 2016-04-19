package org.switchyard.quickstarts.camel.cxf;

import javax.jws.WebService;

@WebService(
        targetNamespace="urn:switchyard-quickstart:camel-cxf:2.0",
        wsdlLocation="WarehouseService.wsdl",
        name="WarehouseService",
        serviceName="WarehouseService",
        portName="WarehouseServicePort")
public interface MyCxfClient {
    public String order(String input);
}
