package org.javaee7.jms.basic.simple;

import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.javaee7.jms.basic.JmsResources;

@Stateless
public class MessageReceiverSync {
	@Inject
	private JMSContext context;

	@Resource(mappedName = JmsResources.SYNC_CONTAINER_MANAGED_QUEUE)
	Queue myQueue;

	/**
	 * Waits to receive a message from the JMS queue. Times out after a given
	 * number of milliseconds.
	 *
	 * @param timeoutInMillis The number of milliseconds this method will wait
	 * before throwing an exception.
	 * @return The contents of the message.
	 * @throws JMSRuntimeException if an error occurs in accessing the queue.
	 * @throws TimeoutException if the timeout is reached.
	 */
	public String receiveMessage(int timeoutInMillis) throws JMSRuntimeException, TimeoutException {
		String message = context.createConsumer(myQueue).receiveBody(String.class, timeoutInMillis);
		if (message == null) {
			throw new TimeoutException("No message received after " + timeoutInMillis + "ms");
		}
		return message;
	}

	public void receiveAll(int timeoutInMillis) throws JMSException {
		System.out.println("--> Receiving redundant messages ...");
		QueueBrowser browser = context.createBrowser(myQueue);
		while (browser.getEnumeration().hasMoreElements()) {
			System.out.println("--> here is one");
			context.createConsumer(myQueue).receiveBody(String.class, timeoutInMillis);
		}
	}
}
