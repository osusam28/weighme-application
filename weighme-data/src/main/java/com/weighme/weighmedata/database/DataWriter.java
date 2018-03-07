package com.weighme.weighmedata.database;

import com.google.pubsub.v1.PubsubMessage;

public interface DataWriter {
	public void write(String data);
}
