/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License, v.2.1 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.switchyard.issues.SWITCHYARD1285.jms;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.switchyard.annotations.Requires;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.switchyard.policy.TransactionPolicy;

/**
 * A POJO Service implementation.
 */
@Service(name="AnotherTransactionService", value=StoreService.class)
@Requires(transaction={TransactionPolicy.SUSPENDS_TRANSACTION, TransactionPolicy.MANAGED_TRANSACTION_GLOBAL})
public class AnotherTransactionServiceBean implements StoreService {

    @Inject @Reference("AnotherTransactionStoreService") @Requires(transaction=TransactionPolicy.PROPAGATES_TRANSACTION)
    private StoreService _storeQueue;
    
    @Override
    public final void store(final String message) {
        _storeQueue.store(message);

        try {
            UserTransaction tx = (UserTransaction)new InitialContext().lookup("java:jboss/UserTransaction");
            if (tx.getStatus() == Status.STATUS_ACTIVE) {
                System.out.println("AnotherTransactionStoreServiceBean: Transaction is ACTIVE - it will be committed - transaction=" + tx);
            } else {
                System.out.println("AnotherTransactionStoreServiceBean: Transaction status is '" + tx.getStatus() + "' - something wrong! - transaction=" + tx);
            }
        } catch (Exception e) {
            System.out.println("AnotherTransactionStoreServiceBean: Failed to set rollback only flag on current transaction");
            e.printStackTrace();
        }

    }
}
