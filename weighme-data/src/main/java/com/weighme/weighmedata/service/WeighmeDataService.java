package com.weighme.weighmedata.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.weighme.weighmedata.database.WeighEventDatastoreWriter;
import com.weighme.weighmedata.pubsub.MessageHandler;
import com.weighme.weighmedata.pubsub.MessageHandlerRunner;

@Service
public class WeighmeDataService {
	
	private Map<String, MessageHandlerRunner> handlers;
	
	public WeighmeDataService() {
		handlers = new HashMap<String, MessageHandlerRunner>();
	}
	
	public void startDataService(String projectId, String kind, String... subscriptionIds) {
		for(String subscriptionId : subscriptionIds) {
			MessageHandler handler = new MessageHandler.Builder(
					projectId, subscriptionId).build();
			
			WeighEventDatastoreWriter writer = null;
			if(subscriptionId.equals("weighme-test")) {
				writer = new WeighEventDatastoreWriter
						.Builder(projectId, kind).build();
			}
			
			MessageHandlerRunner runner = new MessageHandlerRunner(handler, writer);
			runner.start();
			
			handlers.put(subscriptionId, runner);
		}
	}
	
	public void stopDataService(String... subscriptionNames) {
		for(String subscriptionName : subscriptionNames) {
			handlers.get(subscriptionName).stopMessageProcess();
			handlers.remove(subscriptionName);
		}
	}
}
