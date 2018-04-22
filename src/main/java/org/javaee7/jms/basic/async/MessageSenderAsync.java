package org.javaee7.jms.basic.async;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.CompletionListener;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.Queue;

import org.javaee7.jms.basic.JmsResources;


/**
 * Asynchronous message sending is not supported in Java EE 7.
 *
 */
@Stateless
public class MessageSenderAsync {

    @Inject
    //    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    JMSContext context;

    @Resource(lookup = JmsResources.ASYNC_QUEUE)
    Queue asyncQueue;

    /**
     * Send a message to the JMS queue. Prin
     *
     * @param message the contents of the message.
     * @throws JMSRuntimeException if an error occurs in accessing the queue.
     */
    public void sendMessage(String message) throws JMSRuntimeException {
        JMSProducer producer = context.createProducer();
        
        try {
            producer.setAsync(new CompletionListener() {
                @Override
                public void onCompletion(Message msg) {
                    try {
                        System.out.println(msg.getBody(String.class));
                    } catch (JMSException ex) {
                        Logger.getLogger(MessageSenderAsync.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void onException(Message msg, Exception e) {
                    try {
                        System.out.println(msg.getBody(String.class));
                    } catch (JMSException ex) {
                        Logger.getLogger(MessageSenderAsync.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (JMSRuntimeException ex) {
            System.out.println("Caught RuntimeException trying to invoke setAsync - not permitted in Java EE. Resorting to synchronous sending...");
        }

        producer.send(asyncQueue, message);
    }
}
