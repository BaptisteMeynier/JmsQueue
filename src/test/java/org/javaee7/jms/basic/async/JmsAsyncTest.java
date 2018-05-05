package org.javaee7.jms.basic.async;


import java.io.File;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.javaee7.jms.basic.JmsResources;
import org.javaee7.jms.basic.utils.ReceptionSynchronizer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JmsAsyncTest {

	@EJB
	MessageSenderAsync asyncSender;

	@Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
	ConnectionFactory connectionFactory;

	private final int messageReceiveTimeoutInMillis = 10000;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	@Inject
	private MessageListener messageListener;

	@Test
	public void testAsync() throws InterruptedException {
		asyncSender.sendMessage("Fire!");
		ReceptionSynchronizer.waitFor(MessageReceiverAsync.class, "onMessage" , messageReceiveTimeoutInMillis);
		// unless we timed out, the test passes
	}

	@Test
	public void testAsyncWithProgramatic() throws InterruptedException, JMSException {

		try (Connection connection = connectionFactory.createConnection()) {
			connection.start();
			final Session session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(JmsResources.ASYNC_QUEUE);
			final MessageProducer messageProducer = session.createProducer(queue);
			final String payload ="A content";

			MessageConsumer mc= session.createConsumer(queue);
			mc.setMessageListener(messageListener);

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("debut tache " + Thread.currentThread().getName());
					IntStream.range(0, 10).forEach(i->{
						try {
							TimeUnit.SECONDS.sleep(2);
							TextMessage textMessage = session.createTextMessage(payload);
							messageProducer.send(textMessage);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (JMSException e) {
							e.printStackTrace();
						}});
					System.out.println("fin tache");
				}
			});
			
			mc.receive(1000);
			
		}
	}


	@Deployment
	public static WebArchive deploy() {
		return ShrinkWrap.create(WebArchive.class)
				.addClass(MessageSenderAsync.class)
				.addClass(JmsResources.class)
				.addClass(MessageReceiverAsync.class)
				.addClass(ReceptionSynchronizer.class)
				//      .addAsWebInfResource(new File("src/test/resources","WEB-INF/ejb-jar.xml"));
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource(new File("src/test/resources","WEB-INF/jboss-ejb3.xml"));
	}

}
