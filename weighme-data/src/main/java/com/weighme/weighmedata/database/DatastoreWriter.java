package com.weighme.weighmedata.database;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

public class DatastoreWriter implements DataWriter {
	
	private Datastore datastore;
	private String projectId;
	private String kind;
	
	private DatastoreWriter(Builder builder) {
		this.projectId = builder.projectId;
		this.kind = builder.kind;
	}
	
	public void write(String data) {
		datastore = DatastoreOptions.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
		
		System.out.println("Datastore API instantiated ...");
		
		KeyFactory keyFactory = datastore.newKeyFactory()
				.setKind(kind); 
		
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity task = Entity.newBuilder(key)
				.set("eventTimestamp", Timestamp.now())
				.set("deviceName", "photon1")
				.set("weight", 0.023)
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
		
		public DatastoreWriter build() {
			return new DatastoreWriter(this);
		}
	}
}
