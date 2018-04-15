package org.javaee7.jms.basic.simple;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

import org.javaee7.jms.basic.JmsResources;

@Stateless
public class MessageSenderSync {

	    @Inject
	    private JMSContext context;

	    @Resource(mappedName = JmsResources.SYNC_CONTAINER_MANAGED_QUEUE)
	    Queue syncQueue;

	    /**
	     * Send a message to the JMS queue.
	     *
	     * @param message the contents of the message.
	     * @throws JMSRuntimeException if an error occurs in accessing the queue.
	     */
	    public void sendMessage(String message) throws JMSRuntimeException {
	        context.createProducer().send(syncQueue, message);
	    }
	
}
