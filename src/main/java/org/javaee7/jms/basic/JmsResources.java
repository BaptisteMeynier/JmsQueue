package org.javaee7.jms.basic;

import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

/**
 * Application scoped JMS resources for the samples.
 */
@JMSDestinationDefinitions({
    @JMSDestinationDefinition(
        name = JmsResources.CLASSIC_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "classicQueue",
        description = "My Sync Queue"),
    @JMSDestinationDefinition(name = JmsResources.ASYNC_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "asyncQueue",
        description = "My Async Queue"),
    @JMSDestinationDefinition(name = JmsResources.SYNC_APP_MANAGED_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "syncAppQueue",
        description = "My Sync Queue for App-managed JMSContext"),
    @JMSDestinationDefinition(name = JmsResources.SYNC_CONTAINER_MANAGED_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "syncContainerQueue",
        description = "My Sync Queue for Container-managed JMSContext")
})
public class JmsResources {
	public static final String SYNC_APP_MANAGED_QUEUE = "java:app/jms/mySyncAppQueue";
	public static final String SYNC_CONTAINER_MANAGED_QUEUE = "java:app/jms/mySyncContainerQueue";
	public static final String ASYNC_QUEUE = "java:app/jms/myAsyncQueue";
	public static final String CLASSIC_QUEUE = "java:app/jms/classicQueue";
}
