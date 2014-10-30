package org.switchyard.quickstarts.remoteinvoker;

import org.switchyard.component.bean.Service;

@Service(ExceptionService.class)
public class ExceptionBean implements ExceptionService {

    @Override
    public String invoke(String hello) {
        throw new RuntimeException("foobar");
    }

}
