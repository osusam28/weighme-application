package com.weighme.weighmedata.pubsub;

import com.google.pubsub.v1.PubsubMessage;
import com.weighme.weighmedata.database.EventWriter;

public class MessageHandlerRunner extends Thread {
	
	private MessageHandler handler;
	private EventWriter writer;
	private volatile boolean running = true;
	
	public MessageHandlerRunner(MessageHandler handler, EventWriter writer) {
		this.handler = handler;
		this.writer = writer;
	}
	
	@Override
	public void run() {
		try {
			handler.startMessageProcess();
			System.out.println("Message process started for " + 
					handler.getSubscriptionName() + " ...");
		} catch (Exception e) {
			System.out.println("Message process failed ... " + e.getMessage());
		}
		
		while(running) {
			try {
				PubsubMessage message = handler.getMessage();
				System.out.println("MESSAGE: " + message.getData().toStringUtf8());
				writer.write(message.getData().toStringUtf8());
			} catch (Exception e) {
				e.printStackTrace();
				stopMessageHandler();
			}
		}
		
		stopMessageHandler();
	}
	
	public void stopMessageProcess() {
		running = false;
	}
	
	private void stopMessageHandler() {
		if(handler.isRunning()) {
			System.out.println("Stopping message process");
			handler.stopMessageProcess();
		}
	}
}
