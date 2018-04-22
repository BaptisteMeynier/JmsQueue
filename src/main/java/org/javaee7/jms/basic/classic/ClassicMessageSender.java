package org.javaee7.jms.basic.classic;


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.javaee7.jms.basic.JmsResources;

/**
 * Sending a message using classic JMS API.
 *
 */
@Stateless
public class ClassicMessageSender {

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(mappedName = JmsResources.CLASSIC_QUEUE)
    Queue demoQueue;

    /**
     * Send a message to the JMS queue.
     *
     * @param payload the contents of the message.
     * @throws JMSException if an error occurs in accessing the queue.
     */
    public void sendMessage(String payload) throws JMSException {
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            Session session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(demoQueue);
            TextMessage textMessage = session.createTextMessage(payload);
            messageProducer.send(textMessage);
        }
    }
}
