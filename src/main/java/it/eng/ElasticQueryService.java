package it.eng;

import java.util.Collections;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticQueryService {
 
    public static String value="";
    
    @Autowired
    ScheduledTasks scheduledTasks;

    public ElasticQueryService() {
      
    }
    
   
    public String query() {
    	
    	System.out.println("value:"+value);
    	
    	RestTemplate restTemplate = new RestTemplate();

    	// create headers
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    	// add basic authentication
    	headers.setBasicAuth("elastic", "changeme");

    	// build the request
    	
    	//String url = "localhost:9200/logstash*/_search?";
    	String url = "http://ip172-18-0-48-bu3fq41qckh000bkmntg-9200.direct.labs.play-with-docker.com/logstash*/_search?";
    	
    	
		String requestJson="{\r\n" + 
				"   \"size\":100,\r\n" + 
				"   \"query\":{\r\n" + 
				"      \"bool\":{\r\n" + 
				"         \"must\":[\r\n" + 
				"            {\r\n" + 
				"               \"range\":{\r\n" + 
				"                  \"@timestamp\":{\r\n" + 
				"                     \"gte\":\"2020-10-14 00:00:00.000\",\r\n" + 
				"                     \"lte\":\"2020-10-14 23:59:59.999\",\r\n" + 
				"                     \"format\":\"yyyy-MM-dd HH:mm:ss.SSS\"\r\n" + 
				"                  }\r\n" + 
				"               }\r\n" + 
				"            },\r\n" + 
				"            {\r\n" + 
				"               \"match\":{\r\n" + 
				"                  \"user\":\"vale\"\r\n" + 
				"               }\r\n" + 
				"            }\r\n" + 
				"         ]\r\n" + 
				"      }\r\n" + 
				"   }\r\n" + 
				"}";
		// send POST request
    	HttpEntity<String> request = new HttpEntity<String>(requestJson, headers);
    	ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    	// check response
    	if (response.getStatusCode() == HttpStatus.OK) {
    	    System.out.println("Login Successful");
    	    String body = response.getBody();
    	    JSONObject jsonObject = new JSONObject(body);
    	    JSONObject gb = jsonObject.getJSONObject("hits").getJSONObject("total");
    	    
    	    int value = gb.getInt("value");
    	    System.out.println("value:"+value);
    	    if(value>3) {
    	    	scheduledTasks.sendEmail();
    	    	System.out.println("sent mail!!!!!");
    	    }
    	    
    	} else {
    	    System.out.println("Login Failed");
    	}
    	
    	
    	
    	return "ok";
        
    }


}