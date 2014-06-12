package com.example.switchyard.switchyard_example;

import javax.naming.InitialContext;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.switchyard.component.bean.Service;

@Service(RollbackService.class)
public class RollbackServiceBean implements RollbackService {

    private static final String JNDI_TRANSACTION_MANAGER = "java:jboss/TransactionManager";
    private int rollbackCounter = 0;

    @Override
    public void rollback(String name) {
        System.out.println("Doing rollback");
        Transaction t = getCurrentTransaction();
        System.out.println(t);
        doRollback(t);
    }

    protected Transaction getCurrentTransaction() {
        try {
            TransactionManager tm = (TransactionManager) new InitialContext().lookup(JNDI_TRANSACTION_MANAGER);
            Transaction t = tm.getTransaction();
            if (t == null) {
                System.err.println("No active transaction");
            }
            return t;
        } catch (Exception e) {
            System.err.println("Failed to get current transaction");
            return null;
        }
    }

    protected void doRollback(Transaction t) {
        try {
            // rollback, rollback, commit
            if (++rollbackCounter % 3 != 0) {
                t.setRollbackOnly();
                System.out.println("Marked transaction to rollback!");
            } else {
                System.out.println("Rollbacks completed - will be committed");
            }
        } catch (Exception e) {
            System.err.println("Failed to rollback transaction " + e);
        }
    }

}
