package com.remoteTMproject.model.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestJson {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException {
		// TODO Auto-generated method stub
        JsonTransfer json1 = new JsonTransfer();
        Response para1=new Response("1212121",0,"no error",0);
        JSONObject response1=json1.objecToJson(para1);
        System.out.println(response1);
        
	}

}
