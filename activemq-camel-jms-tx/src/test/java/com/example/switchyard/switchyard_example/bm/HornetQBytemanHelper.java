package com.example.switchyard.switchyard_example.bm;

import javax.resource.spi.ConnectionRequestInfo;
import javax.security.auth.Subject;

import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.ra.HornetQRAConnectionFactoryImpl;
import org.hornetq.ra.HornetQRAManagedConnection;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

public class HornetQBytemanHelper {

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
    
}
