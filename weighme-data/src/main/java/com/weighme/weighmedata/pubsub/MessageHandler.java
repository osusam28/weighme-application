package com.weighme.weighmedata.pubsub;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.PubsubMessage;
import com.weighme.weighmedata.database.DataWriter;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class MessageHandler {
	  private String projectId;
	  private String subscriptionId;
	  private DataWriter writer;
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
		  
		  if(builder.writer != null) {
			  this.writer = builder.writer;
		  }
	  }
	
	  // Receive messages over a subscription.
	  public void startMessageProcess() throws Exception {
	    ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
	    subscriber = null;
	    try {
	      // create a subscriber bound to the asynchronous message receiver
	      subscriber =
	          Subscriber.newBuilder(subscriptionName, new MessageReceiverWorker()).build();
	      
	      System.out.println("SUBSCRIPTION NAME: " + subscriber.getSubscriptionName());
	      
	      subscriber.startAsync().awaitRunning();
	      // Continue to listen to messages
	      while (true) {
	        PubsubMessage message = message_queue.take();
	        
	        System.out.println("Message Id: " + message.getMessageId());
	        System.out.println("Data: " + message.getData().toStringUtf8());
	      }
	    } finally {
	      if (subscriber != null) {
	        subscriber.stopAsync();
	      }
	    }
	  }
	  
	  public void stopMessageProcess() {
		  if(subscriber == null) {
			  System.out.println("Subscriber is null");
		  }
		  else if(subscriber.isRunning()) {
			  subscriber.stopAsync();
		  }
	  }
	  
	  public boolean isRunning() {
		  return subscriber.isRunning();
	  }
	  
	  public static class Builder {
		  
		  private String projectId = null;
		  private String subscriptionId = null;
		  private DataWriter writer = null;
		  
		  public Builder(String projectId, String subscriptionId) {
			  this.projectId = projectId;
			  this.subscriptionId = subscriptionId;
		  }
		  
		  public Builder() {
			  this.projectId = "weighme-dev";
			  this.subscriptionId = "weighme-test";
		  }
		  
		  public Builder setDataWriter(DataWriter writer) {
			  this.writer = writer;
			  return this;
		  }
		  
		  public MessageHandler build() {
			  return new MessageHandler(this);
		  }
	  }
}
