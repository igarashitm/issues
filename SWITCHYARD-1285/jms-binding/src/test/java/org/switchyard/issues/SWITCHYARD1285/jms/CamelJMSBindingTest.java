package org.switchyard.issues.SWITCHYARD1285.jms;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.bean.config.model.BeanSwitchYardScanner;
import org.switchyard.test.BeforeDeploy;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.component.test.mixins.hornetq.HornetQMixIn;
import org.switchyard.component.test.mixins.jca.JCAMixIn;
import org.switchyard.component.test.mixins.jca.ResourceAdapterConfig;

/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

@SwitchYardTestCaseConfig(
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML, 
        mixins = {CDIMixIn.class, HornetQMixIn.class, JCAMixIn.class},
        scanners = BeanSwitchYardScanner.class)
@RunWith(SwitchYardRunner.class)
public class CamelJMSBindingTest {
    
    private static final String QUEUE_IN = "GreetingServiceQueue";
    private static final String QUEUE_OUT_GREET = "GreetingStoreQueue";
    private static final String QUEUE_OUT_ANOTHER = "AnotherTransactionStoreQueue";
    private HornetQMixIn _hqMixIn;
    private JCAMixIn _jcaMixIn;
    
    @BeforeDeploy
    public void before() {
        ResourceAdapterConfig ra = new ResourceAdapterConfig(ResourceAdapterConfig.ResourceAdapterType.HORNETQ);
        _jcaMixIn.deployResourceAdapters(ra);
    }

    @Test
    public void testJMSBinding() throws Exception {
        Session session = _hqMixIn.createJMSSession();
        MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(QUEUE_IN));
        TextMessage textMsg = session.createTextMessage();
        textMsg.setText("SwitchYard");
        producer.send(textMsg);
        session.close();
        
        session = _hqMixIn.createJMSSession();
        MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(QUEUE_OUT_GREET));
        assertStoredMessage(consumer.receive(30000));
        Assert.assertNull(consumer.receive(1000));

        consumer = session.createConsumer(HornetQMixIn.getJMSQueue(QUEUE_OUT_ANOTHER));
        assertStoredMessage(consumer.receive(1000));
        assertStoredMessage(consumer.receive(1000));
        assertStoredMessage(consumer.receive(1000));
        assertStoredMessage(consumer.receive(1000));
        Assert.assertNull(consumer.receive(1000));

        session.close();
        Thread.sleep(1000);
    }
    
    private void assertStoredMessage(Message msg) throws Exception {
        Assert.assertNotNull(msg);
        String result = null;
        if (msg instanceof TextMessage) {
            result = TextMessage.class.cast(msg).getText();
        } else if (msg instanceof ObjectMessage) {
            result = ObjectMessage.class.cast(msg).getObject().toString();
        } else {
            Assert.fail("Message is not TextMessage nor ObjectMessage, but is " + msg);
        }
        Assert.assertEquals("Hello there, SwitchYard :-)", result);
    }
}
