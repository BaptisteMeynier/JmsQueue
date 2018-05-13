package org.javaee7.jms.basic.async;


import java.util.concurrent.TimeUnit;


import javax.ejb.EJB;
import javax.jms.JMSException;

import org.javaee7.jms.basic.JmsResources;
import org.javaee7.jms.basic.async.listener.FooMessageListener;
import org.javaee7.jms.basic.async.listener.FooMessageListenerException;
import org.javaee7.jms.basic.classic.ClassicMessageSender;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JmsAsyncTest {

	@EJB
	ClassicMessageSender classicMessageSender;

	@Test
	@InSequence(1)
	public void testAsync() throws InterruptedException, JMSException {
		for (int i=0; i<5;i++) {
			classicMessageSender.sendMessage("HELLO");
			TimeUnit.SECONDS.sleep(1);
		}
	}

	@Deployment
	public static WebArchive deploy() {
		return ShrinkWrap.create(WebArchive.class)
				.addClass(ClassicMessageSender.class)
				.addClass(JmsResources.class)
				.addClass(FooMessageListener.class)
				.addClass(FooMessageListenerException.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		//.addClass(ReceptionSynchronizer.class)
		//.addAsWebInfResource(new File("src/test/resources","WEB-INF/ejb-jar.xml"));
		//.addAsWebInfResource(new File("src/test/resources","WEB-INF/jboss-ejb3.xml"));
	}

}
