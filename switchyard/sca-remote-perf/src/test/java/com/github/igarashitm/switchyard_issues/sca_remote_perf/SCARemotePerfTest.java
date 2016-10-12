/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.igarashitm.switchyard_issues.sca_remote_perf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.quickstarts.demo.cluster.Application;
import org.switchyard.quickstarts.demo.cluster.Car;
import org.switchyard.quickstarts.demo.cluster.Deal;
import org.switchyard.quickstarts.demo.cluster.Offer;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

/**
 * A simple performance test for SCA remote.
 */
@RunWith(Arquillian.class)
public class SCARemotePerfTest {
    private static final QName SERVICE = new QName("urn:switchyard-quickstart-demo-cluster-dealer:1.0", "Dealer");
    private static final String ENDPOINT = "http://localhost:8080/switchyard-remote";
    private static final int CONCURRENCY = 100;
    private static final int REPEAT = 500;

    @Deployment(name = "credit")
    @TargetsContainer("node2")
    public static Archive<?> createCreditApp() {
        return Maven.resolver().resolve("org.switchyard.quickstarts.demos:switchyard-demo-cluster-credit:jar:2.1.0-SNAPSHOT").withoutTransitivity().asSingle(JavaArchive.class);
    }

    @Deployment(name = "dealer")
    @TargetsContainer("node1")
    public static Archive<?> createDealerApp() {
        return Maven.resolver().resolve("org.switchyard.quickstarts.demos:switchyard-demo-cluster-dealer:jar:2.1.0-SNAPSHOT").withoutTransitivity().asSingle(JavaArchive.class);
    }

    @Test
    public void test() throws Exception {
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>> test >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        List<Client> clients = new ArrayList<Client>();
        for (int i=0; i<CONCURRENCY; i++) {
            Client c = new Client("John Smith -" + i, REPEAT);
            clients.add(c);
        }
        long start = System.currentTimeMillis();
        for (Client c : clients) {
            c.start();
        }
        for (Client c : clients) {
            c.join();
        }
        long end = System.currentTimeMillis();
        
        for (Client c : clients) {
            for (Result r : c.getResults()) {
                if (r.isFault()) {
                    throw new Exception("Failed - " + r.getContent());
                }
            }
        }
        System.out.println(">>>>> Result : " + (1000*CONCURRENCY*REPEAT/(end-start)) + "[tps]");
        System.out.println(">>>>> Duration=" + (end-start) + "[ms], Concurrency=" + CONCURRENCY + ", Repeat=" + REPEAT);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
    
    @Test
    public void testSharedInvoker() throws Exception {
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>> testSharedInvoker >>>>>>>>>>>>>>>>");

        HttpInvoker invoker = new HttpInvoker(ENDPOINT);
        List<Client> clients = new ArrayList<Client>();
        for (int i=0; i<CONCURRENCY; i++) {
            Client c = new Client(invoker, "John Smith -" + i, REPEAT);
            clients.add(c);
        }
        long start = System.currentTimeMillis();
        for (Client c : clients) {
            c.start();
        }
        for (Client c : clients) {
            c.join();
        }
        long end = System.currentTimeMillis();
        
        for (Client c : clients) {
            for (Result r : c.getResults()) {
                if (r.isFault()) {
                    throw new Exception("Failed - " + r.getContent());
                }
            }
        }
        System.out.println(">>>>> Result : " + (1000*CONCURRENCY*REPEAT/(end-start)) + "[tps]");
        System.out.println(">>>>> Duration=" + (end-start) + "[ms], Concurrency=" + CONCURRENCY + ", Repeat=" + REPEAT);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
    
    class Client extends Thread {
        private final String _clientId;
        private final int _repeat;
        private RemoteInvoker _invoker;
        private RemoteMessage _message;
        private List<Result> _results = new ArrayList<Result>();

        public Client(String id, int repeat) {
            this(null, id, repeat);
        }
        
        public Client(HttpInvoker invoker, String id, int repeat) {
            _clientId = id;
            _repeat = repeat;
            _invoker = invoker;
            _message = new RemoteMessage();
            _message.setService(SERVICE).setOperation("offer");
        }
        
        public List<Result> getResults() {
            return _results;
        }
        
        public void run() {
            for(int i=0; i<_repeat; i++) {
                try {
                    String txid = _clientId + "-" + i;
                    Offer offer = createOffer(txid, true);
                    _message.setContent(offer);

                     long start = System.currentTimeMillis();
                    RemoteInvoker invoker = null;
                    if (_invoker != null) {
                        invoker = _invoker;
                    } else {
                        invoker = new HttpInvoker(ENDPOINT);
                    }
                   RemoteMessage reply = invoker.invoke(_message);
                    long end = System.currentTimeMillis();

                    Result r = new Result().setDuration(end - start)
                                           .setContent(reply.isFault(), reply.getContent());
                    if (reply.isFault()) {
                        if (r.getContent() instanceof Throwable) {
                            ((Throwable)r.getContent()).printStackTrace();
                        }
                        assert false : "Failed - " + reply.getContent();
                    }
                    if (reply.getContent() instanceof Deal) {
                        Deal deal = (Deal)reply.getContent();
                        assert txid.equals(deal.getOffer().getApplication().getName())
                                : "Race condition detected - original txid=" + txid
                                + ", but the reply has a name=" + deal.getOffer().getApplication().getName();
                    }
                    _results.add(r);
                } catch (IOException e) {
                    e.printStackTrace();
                    Result r = new Result().setContent(true, e);
                    _results.add(r);
                }
            }
        }
    }
    
    class Result {
        private long _duration = 0L;
        private boolean _fault = false;
        private Object _content;
        public long getDuration() {
            return _duration;
        }
        public Result setDuration(long duration) {
            _duration = duration;
            return this;
        }
        public boolean isFault() {
            return _fault;
        }
        public Result setContent(boolean fault, Object content) {
            _fault = fault;
            _content = content;
            return this;
        }
        public Object getContent() {
            return _content;
        }
    }
    
    private Offer createOffer(String id, boolean acceptable) {
        Application app = new Application();
        app.setName(id);
        app.setCreditScore(acceptable ? 700 : 300);
        Car car = new Car();
        car.setPrice(18000);
        car.setVehicleId("Honda");
        Offer offer = new Offer();
        offer.setApplication(app);
        offer.setCar(car);
        offer.setAmount(17000);
        
        return offer;
    }
    
}
