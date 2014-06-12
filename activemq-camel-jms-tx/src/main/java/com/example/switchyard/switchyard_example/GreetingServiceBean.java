package com.example.switchyard.switchyard_example;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(GreetingService.class)
public class GreetingServiceBean implements GreetingService {

    @Inject
    @Reference("ReferenceGreetingService")
    GreetingService greetingService;

    @Inject
    @Reference("ReferenceGreetingServiceJca")
    GreetingService greetingServiceJca;

    @Inject
    @Reference
    RollbackService rollbackService;

    public void greet(String name) {
        greetingService.greet("Hello " + name + "!");
        greetingServiceJca.greet("Hello " + name + "!");

        rollbackService.rollback(name);
    }

}
