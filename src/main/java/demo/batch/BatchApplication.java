package demo.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "demo.batch.esrepository")
@EnableJpaRepositories(basePackages = "demo.batch.jparepository")
public class BatchApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}
	
}
