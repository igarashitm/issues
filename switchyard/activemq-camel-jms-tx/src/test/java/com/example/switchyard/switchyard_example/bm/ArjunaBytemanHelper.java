package com.example.switchyard.switchyard_example.bm;

import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;

public class ArjunaBytemanHelper {

    public void traceTransactionCommit(TransactionImple tx) {
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionImple.commit() > thread=" + thread + ", transaction=" + tx + ", resources=" + tx.getResources());
        //Thread.dumpStack();
    }

    public void traceTransactionRollback(TransactionImple tx) {
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionImple.rollback() > thread=" + thread + ", transaction=" + tx + ", resources=" + tx.getResources());
        //Thread.dumpStack();
    }
    
    public void traceTransactionEnlistResource(TransactionImple tx, XAResource xares, Object[] params) {
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionImple.enlistResource() > thread=" + thread + ", transaction=" + tx + ", XAResource=" + xares + ", params=" + params);
        //Thread.dumpStack();
    }
    
    public void traceManagerBegin(BaseTransaction manager) {
        TransactionImple tx = TransactionImple.getTransaction();
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionManagerImple.begin() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
    
    public void traceManagerCommit(BaseTransaction manager) {
        TransactionImple tx = TransactionImple.getTransaction();
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionManagerImple.commit() > thread=" + thread + ", transaction=" + tx + ", resources=" + tx.getResources());
        //Thread.dumpStack();
    }
    
    public void traceManagerRollback(BaseTransaction manager) {
        TransactionImple tx = TransactionImple.getTransaction();
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionManagerImple.rollback() > thread=" + thread + ", transaction=" + tx + ", resources=" + tx.getResources());
        //Thread.dumpStack();
    }
    
    public void traceManagerSuspend(TransactionManagerImple manager) {
        TransactionImple tx = TransactionImple.getTransaction();
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionManagerImple.suspend() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
    
    public void traceManagerResume(TransactionManagerImple manager, Transaction itx) {
        TransactionImple tx = TransactionImple.class.cast(itx);
        Thread thread = Thread.currentThread();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionManagerImple.resume() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
}
