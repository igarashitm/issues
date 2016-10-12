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
package org.switchyard.quickstarts.demo.policy.transaction;

import javax.inject.Inject;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.switchyard.runtime.util.TransactionManagerLocator;

/**
 * WorkServiceBean.
 */
@Service(WorkService.class)
public class WorkServiceBean implements WorkService {

    @Inject
    @Reference("StoreService")
    private StoreService _store;

    private static final int DELAY = 33;

    @Override
    public final void doWork() {

        Transaction t = null;
        try {
            t = getCurrentTransaction();
        } catch (Exception e) {
            print("Failed to get current transcation");
        }
        if (t == null) {
            print("No active transaction");
            return;
        }

        _store.store("WorkService :: " + System.currentTimeMillis());

        for (int i=0; i<DELAY; i++) {
            print("Waiting for transaction timeout... " + (i+1) + "/" + DELAY);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                print(e.getMessage());
            }
        }
        print("done.");
    }

    private Transaction getCurrentTransaction() throws Exception {
        TransactionManager tm = TransactionManagerLocator.locateTransactionManager();
        return tm.getTransaction();
    }

    private void print(String message) {
        System.out.println(":: WorkService :: " + message);
    }

}
