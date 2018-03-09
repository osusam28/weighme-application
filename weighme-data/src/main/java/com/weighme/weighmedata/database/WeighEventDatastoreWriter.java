package com.weighme.weighmedata.database;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public class WeighEventDatastoreWriter implements EventWriter {
	private Datastore datastore;
	private KeyFactory keyFactory;
	private String projectId;
	private String kind;
	
	private WeighEventDatastoreWriter(Builder builder) {
		this.projectId = builder.projectId;
		this.kind = builder.kind;
		
		init();
	}
	
	private void init() {
		datastore = DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
		
		keyFactory = datastore.newKeyFactory()
				.setKind(kind);
	}
	
	public void write(String data) throws JsonParseException, IOException {
		JSONTransformer transformer = new JSONTransformer();
		JsonNode node = transformer.StringToJSON(data);
		JsonNode event = node.get("data"); 
		
		System.out.println("Storing data ...");
		
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity task = Entity.newBuilder(key)
				.set("eventTimestamp", Timestamp.now())
				.set("deviceName", event.get("deviceName").textValue())
				.set("weight", event.get("reading").get("weight").textValue())
				.set("offset", event.get("offset").textValue())
				.set("scaler", event.get("scaler").textValue())
				.set("threshold", event.get("threshold").textValue())
				.set("units", event.get("reading").get("units").textValue())
				.build();
		
		datastore.put(task);
	}
	
	public static class Builder {
		private String projectId;
		private String kind;
		
		public Builder(String projectId, String kind) {
			this.projectId = projectId;
			this.kind = kind;
		}
		
		public Builder setKind(String kind) {
			this.kind = kind;
			return this;
		}
		
		public WeighEventDatastoreWriter build() {
			return new WeighEventDatastoreWriter(this);
		}
	}
}
