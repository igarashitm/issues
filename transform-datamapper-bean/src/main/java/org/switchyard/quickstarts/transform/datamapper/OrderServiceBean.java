package org.switchyard.quickstarts.transform.datamapper;

import javax.inject.Inject;

import org.switchyard.Scope;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.ReferenceInvocation;
import org.switchyard.component.bean.ReferenceInvoker;
import org.switchyard.component.bean.Service;

@Service(OrderService.class)
public class OrderServiceBean implements OrderService {

    @Inject @Reference("StoreService")
    public ReferenceInvoker _storeInvoker;
    
    @Override
    public void order(String input) {
        ReferenceInvocation invocation = _storeInvoker.newInvocation("store");
        invocation.getContext().setProperty("approval", "AUTO", Scope.EXCHANGE);
        try {
            invocation.invoke(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
