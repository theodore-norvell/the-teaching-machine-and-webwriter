package com.remoteTMproject.model.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonTransfer {
	
	
	//transfer java object to Json String
   public JSONObject objecToJson(Response para) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException{
	   
	   ObjectMapper objectMapper = new ObjectMapper(); 
	   
	   objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
	// Convert object to JSON string and pretty print
	   String jsonInString = objectMapper.writeValueAsString(para);
	   
       JSONParser parser = new JSONParser();
       
       JSONObject json = (JSONObject) parser.parse(jsonInString);
	   
	   return   json;
   }
	
	
	
}
