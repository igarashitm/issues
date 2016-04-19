An example to configure CXF client via Camel CxfEndpointConfigurer, and set CXF RequestContext property via context property
-----
To run this:
```
    mvn clean test
```

## Expected Result
You'd see following log. Check out the security.signature.properties and security.callback-handler is set as expected.
```
13:02:38,289 INFO  [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer] >>>>> Configuring CXF client: org.apache.camel.component.cxf.CxfEndpoint$CamelCxfClientImpl@77c68c4d
...
13:02:38,258 INFO  [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer] >>>>> OutInterceptor [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer$MyExampleOutInterceptor]
13:02:38,259 INFO  [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer] >>>>> Feature [org.apache.camel.component.cxf.feature.PayLoadDataFormatFeature]
13:02:38,259 INFO  [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer] >>>>> Feature [org.switchyard.quickstarts.camel.cxf.MyExampleCxfEndpointConfigurer$MyExampleFeature]
...
13:02:38,296 DEBUG [org.apache.cxf.endpoint.ClientImpl] set requestContext to message be{org.apache.cxf.message.Message.PROTOCOL_HEADERS={breadcrumbId=[ID-localhost-localdomain-45101-1461038554719-0-16]}, security.callback-handler=org.switchyard.quickstarts.camel.cxf.RequestContextProcessor$1@6258263e, org.apache.cxf.service.model.BindingOperationInfo=[BindingOperationInfo: {urn:switchyard-quickstart:camel-cxf:2.0}order], CamelCreatedTimestamp=Tue Apr 19 13:02:38 JST 2016, security.signature.properties=not-existing-keystore.properties, org.switchyard.faultType={urn:switchyard-quickstart:camel-cxf:2.0}ItemNotAvailable, org.apache.cxf.jaxws.context.WrappedMessageContext.SCOPES={security.callback-handler=APPLICATION, org.apache.cxf.service.model.BindingOperationInfo=APPLICATION, CamelCreatedTimestamp=APPLICATION, security.signature.properties=APPLICATION, org.switchyard.faultType=APPLICATION, org.apache.camel.component.cxf.DataFormat=APPLICATION, CamelCXFDataFormat=APPLICATION, RequestContext=APPLICATION, org.switchyard.operationName=APPLICATION, mtom-enabled=APPLICATION, CamelToEndpoint=APPLICATION, org.switchyard.serviceName=APPLICATION}, org.apache.camel.component.cxf.DataFormat=PAYLOAD, CamelCXFDataFormat=PAYLOAD, RequestContext={security.callback-handler=org.switchyard.quickstarts.camel.cxf.RequestContextProcessor$1@6258263e, security.signature.properties=not-existing-keystore.properties, mtom-enabled=true}, org.switchyard.operationName=order, mtom-enabled=false, CamelToEndpoint=cxf://http://localhost:8083/camel-cxf/warehouse/WarehouseService?cxfEndpointConfigurer=%23myExampleCxfEndpointConfigurer&dataFormat=PAYLOAD&setDefaultBus=false&wsdlURL=WarehouseService.wsdl, org.switchyard.serviceName={urn:switchyard-quickstart:camel-cxf:2.0}WarehouseServiceExternal}
```
