package it.eng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticQueryService {
 
    public static String value="";
    
    @Autowired
    ScheduledTasks scheduledTasks;
    @Value( "${elastic.url}" )
    private String url;
    
    private static boolean read=false;

   

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
    	
    	
    	//Read File Content
    	String content = null;
    	if(!read) {
    		content = readQueryFile();
    	}
    	
    	
    	
    	
	
		// send POST request
    	HttpEntity<String> request = new HttpEntity<String>(content, headers);
    	ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    	// check response
    	if (response.getStatusCode() == HttpStatus.OK) {
    	    System.out.println("Login Successful");
    	    String body = response.getBody();
    	    JSONObject jsonObject = new JSONObject(body);
    	    JSONObject gb = jsonObject.getJSONObject("hits").getJSONObject("total");
    	    
    	    int value = gb.getInt("value");
    	    System.out.println("value:"+value);
    	    if(value>10) {
    	    	scheduledTasks.sendEmail();
    	    	System.out.println("sent mail!!!!!");
    	    }
    	    
    	} else {
    	    System.out.println("Login Failed");
    	}
    	
    	
    	
    	return "ok";
        
    }
    
    
    private String readQueryFile() {
    	
    	
    	//Read File Content
    			String content = null;
    			try {
    				File file = ResourceUtils.getFile("classpath:query.txt");

    				content = new String(Files.readAllBytes(file.toPath()));
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			System.out.println(content);
    			this.read=true;
				return content;
    	
    }


}