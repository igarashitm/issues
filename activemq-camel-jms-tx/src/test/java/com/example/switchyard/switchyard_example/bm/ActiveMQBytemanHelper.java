package com.example.switchyard.switchyard_example.bm;

import org.apache.activemq.ra.ActiveMQConnectionFactory;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

public class ActiveMQBytemanHelper {

    public void traceAMQCreateConnection(ActiveMQConnectionFactory factory) {
        Thread thread = Thread.currentThread();
        TransactionImple tx = TransactionImple.getTransaction();
        System.err.println(">>>>>>>>>> >>>>>>>>>> ActiveMQConnectionFactory.createConnection() > thread=" + thread + ", transaction=" + tx);
        //Thread.dumpStack();
    }
}
