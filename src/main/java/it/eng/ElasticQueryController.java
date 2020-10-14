package it.eng;

import java.net.URI;
import java.util.Collections;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ElasticQueryController {
 
    public static String value="";
    
    @Autowired
    ScheduledTasks scheduledTasks;
    
    @Autowired
    ElasticQueryService elasticQueryService;

    public ElasticQueryController() {
      
    }
    
    @GetMapping("/go/{id}")
    public String getGo(@PathVariable("id") String id) {
    	value = id;
    	System.out.println("value:"+value);
    	
    	String query = elasticQueryService.query();
    	
    	
    	return query;
        
    }

//    @GetMapping("/{id}")
//    public String getSpanishGreetingById(@PathVariable("id") final int id) {
//        return spanishGreetings.get(id - 1); // list index starts with 0 but we prefer to start on 1
//    }
//
//    @GetMapping("/random")
//    public SpanishGreeting getRandom() {
//        return spanishGreetings.get(new Random().nextInt(spanishGreetings.size()));
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.OK)
//    public void createSpanishGreeting(@RequestBody SpanishGreeting spanishGreeting) {
//        spanishGreetings.add(spanishGreeting);
//    }
}