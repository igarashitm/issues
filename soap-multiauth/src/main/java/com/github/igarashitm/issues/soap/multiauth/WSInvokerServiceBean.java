package com.github.igarashitm.issues.soap.multiauth;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(WSInvokerService.class)
public class WSInvokerServiceBean implements WSInvokerService {

    @Inject @Reference("Hello1")
    private HelloService _hello1;
    
    @Inject @Reference("Hello2")
    private HelloService _hello2;
    
    @Override
    public String invokeHello1(String input) {
        return _hello1.sayHello(input);
    }

    @Override
    public String invokeHello2(String input) {
        return _hello2.sayHello(input);
    }

}
