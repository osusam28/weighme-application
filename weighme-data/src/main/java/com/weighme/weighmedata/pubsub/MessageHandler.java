package com.weighme.weighmedata.pubsub;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class MessageHandler {
	  private String projectId;
	  private String subscriptionId;
	  Subscriber subscriber;

	  private static final BlockingQueue<PubsubMessage> message_queue = new LinkedBlockingDeque<>();
	
	  private static class MessageReceiverWorker implements MessageReceiver {
	
	    @Override
	    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
	      message_queue.offer(message);
	      consumer.ack();
	    }
	  }
	  
	  private MessageHandler(Builder builder) {
		  this.projectId = builder.projectId;
		  this.subscriptionId = builder.subscriptionId;
	  }
	
	  // Receive messages over a subscription.
	  public void startMessageProcess() throws Exception {
		  ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
	    
	      // create a subscriber bound to the asynchronous message receiver
	      subscriber =
	          Subscriber.newBuilder(subscriptionName, new MessageReceiverWorker()).build();
	      
	      subscriber.startAsync().awaitRunning();
	  }
	  
	  public void stopMessageProcess() {
		  if(subscriber == null) {
			  System.out.println("Subscriber is null");
		  }
		  else if(subscriber.isRunning()) {
			  subscriber.stopAsync();
		  }
	  }
	  
	  public PubsubMessage getMessage() throws Exception {
		  return message_queue.take();
	  }
	  
	  public boolean isRunning() {
		  if(subscriber == null) {
			  return false;
		  }
		  return subscriber.isRunning();
	  }
	  
	  public String getSubscriptionName() {
		  return subscriber.getSubscriptionName().toString();
	  }
	  
	  public static class Builder {
		  
		  private String projectId = null;
		  private String subscriptionId = null;
		  
		  public Builder(String projectId, String subscriptionId) {
			  this.projectId = projectId;
			  this.subscriptionId = subscriptionId;
		  }
		  
		  public MessageHandler build() {
			  return new MessageHandler(this);
		  }
	  }
}
