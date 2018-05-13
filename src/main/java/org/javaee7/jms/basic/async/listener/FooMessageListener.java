package org.javaee7.jms.basic.async.listener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.javaee7.jms.basic.JmsResources;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",propertyValue = JmsResources.CLASSIC_QUEUE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class FooMessageListener implements MessageListener {


	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("FooMessageListener received "	+ textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}