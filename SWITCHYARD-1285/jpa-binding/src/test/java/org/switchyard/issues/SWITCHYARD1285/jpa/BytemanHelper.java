package org.switchyard.issues.SWITCHYARD1285.jpa;

import javax.resource.spi.ConnectionRequestInfo;
import javax.security.auth.Subject;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.ra.HornetQRAConnectionFactoryImpl;
import org.hornetq.ra.HornetQRAManagedConnection;
import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.handlers.TransactionHandler;

import com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;

public class BytemanHelper {

    public void traceHandlerStartTx(TransactionHandler handler) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionHandler.startTransaction() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }

    public void traceHandlerEndTx(TransactionHandler handler) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionHandler.endTransaction() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }

    public void traceHandlerResumeTx(TransactionHandler handler, Transaction itx) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.class.cast(itx);
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionHandler.resumeTransaction() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }

    public void traceHandlerSuspendTx(TransactionHandler handler, Exchange exchange) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> TransactionHandler.suspendTransaction() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }

    public void traceExchangeSend(Exchange exchange, Message msg) {
        System.err.println(">>>>>>>>>> >>>>>>>>>> Exchange.send() > consumer=" + exchange.getConsumer().getName().getLocalPart() + "." + exchange.getContract().getConsumerOperation().getName());
    }
    
    public void traceHQCreateConnection(HornetQConnectionFactory factory) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> HornetQConnectionFactory.createConnection() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
    
    public void traceHQRACreateConnection(HornetQRAConnectionFactoryImpl factory) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> HornetQRAConnectionFactoryImpl.createConnection() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
    
    public void traceHQGetConnection(HornetQRAManagedConnection hqmc, Subject subject, ConnectionRequestInfo info) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> HornetQRAManagedConnection.getConnection() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
    
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
