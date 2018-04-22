package org.javaee7.jms.basic.classic;


import java.util.concurrent.TimeoutException;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.javaee7.jms.basic.JmsResources;



/**
 * Synchronized message receiver using classic API.
 *
 */
@Stateless
public class ClassicMessageReceiver {

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(mappedName = JmsResources.CLASSIC_QUEUE)
    Queue demoQueue;

    /**
     * Waits to receive a message from the JMS queue. Times out after a given
     * number of milliseconds.
     *
     * @param timeoutInMillis The number of milliseconds this method will wait
     * before throwing an exception.
     * @return The contents of the message.
     * @throws JMSException if an error occurs in accessing the queue.
     * @throws TimeoutException if the timeout is reached.
     */
    public String receiveMessage(int timeoutInMillis) throws JMSException, TimeoutException {
        String response = null;
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer messageConsumer = session.createConsumer(demoQueue);
            Message message = messageConsumer.receive(timeoutInMillis);
            if (message == null) {
                throw new TimeoutException("No message received after " + timeoutInMillis + "ms");
            }
            response = message.getBody(String.class);
        }
        return response;
    }
}

