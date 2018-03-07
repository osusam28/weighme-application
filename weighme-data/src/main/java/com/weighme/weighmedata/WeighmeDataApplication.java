package com.weighme.weighmedata;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.weighme.weighmedata.pubsub.MessageHandler;
import com.weighme.weighmedata.service.WeighmeDataService;

@SpringBootApplication
public class WeighmeDataApplication {
	
	@Autowired
	private WeighmeDataService dataService;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(WeighmeDataApplication.class, args);
		
		//new MessageHandler.Builder().build().startMessageProcess();
	}
	
	public WeighmeDataApplication() {
		
	}
	
	@PostConstruct
	public void init() throws Exception {
		System.out.println("Starting service ...");
		dataService.startDataService();
	}
}
