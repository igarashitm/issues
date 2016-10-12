## A reproducer for ENTESB-5166 without SwitchYard

#### Deploy WebService application
```
mvn -Pdeloy install
```

#### Run a WebService client
```
mvn exec:java
```

#### Undeploy WebService application
```
mvn -Pdeploy clean
```

## Expected output
Decode failed due to SOAP-ENV namespace prefix is not recognized
```
16:31:06,025 WARN  [org.apache.cxf.phase.PhaseInterceptorChain] Interceptor for {http://signencrypt.soap.issues.igarashitm.github.com/}FaultServiceService#{http://signencrypt.soap.issues.igarashitm.github.com/}invoke has thrown exception, unwinding now
org.apache.cxf.binding.soap.SoapFault: The signature or decryption was invalid
	at org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor.createSoapFault(WSS4JInInterceptor.java:892)
	at org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor.handleMessage(WSS4JInInterceptor.java:331)
	at org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JInInterceptor.handleMessage(PolicyBasedWSS4JInInterceptor.java:121)
	at org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JInInterceptor.handleMessage(PolicyBasedWSS4JInInterceptor.java:106)
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:272)
	at org.apache.cxf.endpoint.ClientImpl.onMessage(ClientImpl.java:849)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponseInternal(HTTPConduit.java:1632)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponse(HTTPConduit.java:1520)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.close(HTTPConduit.java:1318)
	at org.apache.cxf.io.CacheAndWriteOutputStream.postClose(CacheAndWriteOutputStream.java:56)
	at org.apache.cxf.io.CachedOutputStream.close(CachedOutputStream.java:223)
	at org.apache.cxf.transport.AbstractConduit.close(AbstractConduit.java:56)
	at org.apache.cxf.transport.http.HTTPConduit.close(HTTPConduit.java:633)
	at org.apache.cxf.interceptor.MessageSenderInterceptor$MessageSenderEndingInterceptor.handleMessage(MessageSenderInterceptor.java:62)
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:272)
	at org.apache.cxf.endpoint.ClientImpl.doInvoke(ClientImpl.java:572)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:481)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:382)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:335)
	at org.apache.cxf.frontend.ClientProxy.invokeSync(ClientProxy.java:96)
	at org.apache.cxf.jaxws.JaxWsClientProxy.invoke(JaxWsClientProxy.java:136)
	at com.sun.proxy.$Proxy40.invoke(Unknown Source)
	at com.github.igarashitm.issues.soap.signencrypt.SOAPClient.main(SOAPClient.java:39)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: org.apache.ws.security.WSSecurityException: The signature or decryption was invalid
	at org.apache.ws.security.processor.ReferenceListProcessor.decryptEncryptedData(ReferenceListProcessor.java:342)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.decryptDataRef(EncryptedKeyProcessor.java:463)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.decryptDataRefs(EncryptedKeyProcessor.java:392)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.handleToken(EncryptedKeyProcessor.java:179)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.handleToken(EncryptedKeyProcessor.java:66)
	at org.apache.ws.security.WSSecurityEngine.processSecurityHeader(WSSecurityEngine.java:402)
	at org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor.handleMessage(WSS4JInInterceptor.java:274)
	... 27 more
Caused by: org.apache.xml.security.encryption.XMLEncryptionException: The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
Original Exception was org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 212; The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:93)
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:49)
	at org.apache.xml.security.encryption.XMLCipher.decryptElement(XMLCipher.java:1625)
	at org.apache.xml.security.encryption.XMLCipher.decryptElementContent(XMLCipher.java:1656)
	at org.apache.xml.security.encryption.XMLCipher.doFinal(XMLCipher.java:978)
	at org.apache.ws.security.processor.ReferenceListProcessor.decryptEncryptedData(ReferenceListProcessor.java:340)
	... 33 more
Caused by: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 212; The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
	at com.sun.org.apache.xerces.internal.parsers.DOMParser.parse(DOMParser.java:257)
	at com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl.parse(DocumentBuilderImpl.java:339)
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:73)
	... 38 more
[WARNING] 
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: javax.xml.ws.soap.SOAPFaultException: The signature or decryption was invalid
	at org.apache.cxf.jaxws.JaxWsClientProxy.invoke(JaxWsClientProxy.java:158)
	at com.sun.proxy.$Proxy40.invoke(Unknown Source)
	at com.github.igarashitm.issues.soap.signencrypt.SOAPClient.main(SOAPClient.java:39)
	... 6 more
Caused by: org.apache.ws.security.WSSecurityException: The signature or decryption was invalid
	at org.apache.ws.security.processor.ReferenceListProcessor.decryptEncryptedData(ReferenceListProcessor.java:342)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.decryptDataRef(EncryptedKeyProcessor.java:463)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.decryptDataRefs(EncryptedKeyProcessor.java:392)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.handleToken(EncryptedKeyProcessor.java:179)
	at org.apache.ws.security.processor.EncryptedKeyProcessor.handleToken(EncryptedKeyProcessor.java:66)
	at org.apache.ws.security.WSSecurityEngine.processSecurityHeader(WSSecurityEngine.java:402)
	at org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor.handleMessage(WSS4JInInterceptor.java:274)
	at org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JInInterceptor.handleMessage(PolicyBasedWSS4JInInterceptor.java:121)
	at org.apache.cxf.ws.security.wss4j.PolicyBasedWSS4JInInterceptor.handleMessage(PolicyBasedWSS4JInInterceptor.java:106)
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:272)
	at org.apache.cxf.endpoint.ClientImpl.onMessage(ClientImpl.java:849)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponseInternal(HTTPConduit.java:1632)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.handleResponse(HTTPConduit.java:1520)
	at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.close(HTTPConduit.java:1318)
	at org.apache.cxf.io.CacheAndWriteOutputStream.postClose(CacheAndWriteOutputStream.java:56)
	at org.apache.cxf.io.CachedOutputStream.close(CachedOutputStream.java:223)
	at org.apache.cxf.transport.AbstractConduit.close(AbstractConduit.java:56)
	at org.apache.cxf.transport.http.HTTPConduit.close(HTTPConduit.java:633)
	at org.apache.cxf.interceptor.MessageSenderInterceptor$MessageSenderEndingInterceptor.handleMessage(MessageSenderInterceptor.java:62)
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:272)
	at org.apache.cxf.endpoint.ClientImpl.doInvoke(ClientImpl.java:572)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:481)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:382)
	at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:335)
	at org.apache.cxf.frontend.ClientProxy.invokeSync(ClientProxy.java:96)
	at org.apache.cxf.jaxws.JaxWsClientProxy.invoke(JaxWsClientProxy.java:136)
	... 8 more
Caused by: org.apache.xml.security.encryption.XMLEncryptionException: The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
Original Exception was org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 212; The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:93)
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:49)
	at org.apache.xml.security.encryption.XMLCipher.decryptElement(XMLCipher.java:1625)
	at org.apache.xml.security.encryption.XMLCipher.decryptElementContent(XMLCipher.java:1656)
	at org.apache.xml.security.encryption.XMLCipher.doFinal(XMLCipher.java:978)
	at org.apache.ws.security.processor.ReferenceListProcessor.decryptEncryptedData(ReferenceListProcessor.java:340)
	... 33 more
Caused by: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 212; The prefix "SOAP-ENV" for element "SOAP-ENV:Fault" is not bound.
	at com.sun.org.apache.xerces.internal.parsers.DOMParser.parse(DOMParser.java:257)
	at com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl.parse(DocumentBuilderImpl.java:339)
	at org.apache.xml.security.encryption.DocumentSerializer.deserialize(DocumentSerializer.java:73)
	... 38 more
```
