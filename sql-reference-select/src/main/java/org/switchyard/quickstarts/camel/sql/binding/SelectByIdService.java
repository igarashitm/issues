package org.switchyard.quickstarts.camel.sql.binding;

public interface SelectByIdService {
    Greeting[] select(String id);
}
