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
import javax.transaction.UserTransaction;

import org.switchyard.annotations.Requires;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.switchyard.policy.TransactionPolicy;

/**
 * A POJO Service implementation.
 */
@Service(GreetingService.class)
@Requires(transaction=TransactionPolicy.PROPAGATES_TRANSACTION)
public class GreetingServiceBean implements GreetingService {

    @Inject @Reference("GreetingStoreService") @Requires(transaction=TransactionPolicy.PROPAGATES_TRANSACTION)
    private StoreService _greetingQueue;
    @Inject @Reference("AnotherTransactionService") @Requires(transaction=TransactionPolicy.SUSPENDS_TRANSACTION)
    private StoreService _anotherTx;
    private int _rollbackCounter = 0;
    
    @Override
    public final void greet(final String name) {
        String message = "Hello there, " + name + " :-)";
        _greetingQueue.store(message);
        _anotherTx.store(message);

        try {
            UserTransaction tx = (UserTransaction)new InitialContext().lookup("/UserTransaction");
            if (++_rollbackCounter % 4 != 0) {
                tx.setRollbackOnly();
                System.out.println("GreetingServiceBean: rollback counter=" + _rollbackCounter + ", set rollback only flag - transaction=" + tx);
            } else {
                System.out.println("GreetingServiceBean: rollback counter=" + _rollbackCounter + ", will be committed - transaction=" + tx);
            }
        } catch (Exception e) {
            System.out.println("GreetingServiceBean: Failed to set rollback only flag on current transaction");
            e.printStackTrace();
        }

    }
}
