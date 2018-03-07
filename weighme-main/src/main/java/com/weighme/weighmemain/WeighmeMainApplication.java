package com.weighme.weighmemain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeighmeMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeighmeMainApplication.class, args);
	}
	
	/*
	Query<Entity> query = Query.newEntityQueryBuilder()
			.setKind("device-event-detail")
			.build();
	
	Iterator<Entity> results = datastore.run(query);
	
	while(true) {
		if(!results.hasNext()) {
			System.out.println("No more results.");
			break;
		}
		
		Entity result = results.next();
		
		//System.out.println(result.toString());
		System.out.println("KIND: " + result.getKey().getKind());
		System.out.println("PROJECT ID: " + result.getKey().getProjectId());
		System.out.println("ID: " + result.getKey().getId());
		
		System.out.println("PROPERTIES: " + result.getString("deviceName"));
	}
	*/
}
