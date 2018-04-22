package org.javaee7.jms.basic.sync.appmanaged;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

import org.javaee7.jms.basic.JmsResources;


/**
 * Synchronous message sending with app-managed JMSContext. JMSContext can be
 * used with try-with-resources construct.
 *
 */
@Stateless
public class MessageSenderAppManaged {

    @Resource
    private ConnectionFactory factory;

    @Resource(mappedName = JmsResources.SYNC_APP_MANAGED_QUEUE)
    Queue myQueue;

    /**
     * Send a message to the JMS queue.
     *
     * @param message the contents of the message.
     * @throws JMSRuntimeException if an error occurs in accessing the queue.
     */
    public void sendMessage(String message) throws JMSRuntimeException {
        try (JMSContext context = factory.createContext()) {
            context.createProducer().send(myQueue, message);
        }
    }
}
