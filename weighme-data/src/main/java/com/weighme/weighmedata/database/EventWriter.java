package com.weighme.weighmedata.database;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;

public interface EventWriter {
	public void write(String data) throws JsonParseException, IOException;
}
