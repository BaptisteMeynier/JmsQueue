package org.javaee7.jms.basic.async;

import org.javaee7.jms.basic.JmsResources;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * A message driven bean with newly standardized activation config properties.
 *
 */
@MessageDriven(name = "MDBExample", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "app/jms/myAsyncQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class MessageReceiverAsync implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("PLOUF");
        try {
            TextMessage tm = (TextMessage) message;

            System.out.println("Message received (async): " + tm.getText());
        } catch (JMSException ex) {
            Logger.getLogger(MessageReceiverAsync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
