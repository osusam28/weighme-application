package com.weighme.weighmedata.service;

import org.springframework.stereotype.Service;

import com.weighme.weighmedata.pubsub.MessageHandler;

@Service
public class WeighmeDataService {
	
	private MessageHandler handler;
	private String projectId;
	private String subscriptionId;
	
	public WeighmeDataService() {
		projectId = "weighme-dev";
		subscriptionId = "weighme-test";
	}
	
	public void startDataService() {
		
		if(handler.isRunning()) {
			System.out.println("Already running ...");
			return;
		}
		
		handler = new MessageHandler.Builder(
				projectId, subscriptionId).build();
		
		try {
			handler.startMessageProcess();
		}
		catch (Exception e) {
			System.out.println("Error occured when starting message process ...");
		}
	}
	
	public void stopDataService() {
		if(handler.isRunning()) {
			System.out.println("Already stopped ...");
			return;
		}
		
		handler.stopMessageProcess();
	}
}
