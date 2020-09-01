package demo.batch.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
	
	@Bean
	public RestClient client() {
		
		return RestClient.builder(
			new HttpHost("192.168.243.15", 9200, "http")).build();
		
	}
	
	
}