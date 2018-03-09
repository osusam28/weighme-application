package com.weighme.weighmedata.database;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTransformer {
	ObjectMapper mapper;
	
	public JSONTransformer() {
		mapper = new ObjectMapper();
	}
	
	public JsonNode StringToJSON(String string) throws IOException {
		JsonNode node = mapper.readTree(string);
		return node;
	}
}
