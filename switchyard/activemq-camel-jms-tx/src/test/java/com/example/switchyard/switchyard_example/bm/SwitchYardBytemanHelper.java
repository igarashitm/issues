package com.example.switchyard.switchyard_example.bm;

import javax.transaction.Transaction;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.handlers.TransactionHandler;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

public class SwitchYardBytemanHelper {

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
    
}
