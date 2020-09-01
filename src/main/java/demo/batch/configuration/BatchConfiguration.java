package demo.batch.configuration;

import demo.batch.domain.Ratings;
import demo.batch.infrastructure.ColumnRangePartitioner;
import demo.batch.infrastructure.RatingsRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.partition.BeanFactoryStepLocator;
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler;
import org.springframework.batch.integration.partition.StepExecutionRequestHandler;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration implements ApplicationContextAware {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	public DataSource dataSource;
	@Autowired
	public JobExplorer jobExplorer;
	@Autowired
	public JobRepository jobRepository;
	
	private ApplicationContext applicationContext;
	
	private static final int GRID_SIZE = 4;
	
	private static int alreadyread = 0;
	
	@Bean
	public PartitionHandler partitionHandler(MessagingTemplate messagingTemplate) throws Exception {
		MessageChannelPartitionHandler partitionHandler = new MessageChannelPartitionHandler();
		
		partitionHandler.setStepName("slaveStep");
		partitionHandler.setGridSize(GRID_SIZE);
		partitionHandler.setMessagingOperations(messagingTemplate);
		partitionHandler.setPollInterval(5000L);
		partitionHandler.setJobExplorer(this.jobExplorer);
		
		partitionHandler.afterPropertiesSet();
		
		return partitionHandler;
	}
	
	@Bean
	public ColumnRangePartitioner partitioner() {
		ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
		
		columnRangePartitioner.setColumn("tconst");
		columnRangePartitioner.setDataSource(this.dataSource);
		columnRangePartitioner.setTable("ratings");
		
		return columnRangePartitioner;
	}
	
	@Bean
	@Profile("slave")
	@ServiceActivator(inputChannel = "inboundRequests", outputChannel = "outboundStaging")
	public StepExecutionRequestHandler stepExecutionRequestHandler() {
		StepExecutionRequestHandler stepExecutionRequestHandler =
			new StepExecutionRequestHandler();
		
		BeanFactoryStepLocator stepLocator = new BeanFactoryStepLocator();
		stepLocator.setBeanFactory(this.applicationContext);
		stepExecutionRequestHandler.setStepLocator(stepLocator);
		stepExecutionRequestHandler.setJobExplorer(this.jobExplorer);
		
		return stepExecutionRequestHandler;
	}
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {
		PollerMetadata pollerMetadata = new PollerMetadata();
		pollerMetadata.setTrigger(new PeriodicTrigger(10));
		return pollerMetadata;
	}
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Ratings> pagingItemReader(
		@Value("#{stepExecutionContext['minValue']}") String minValue,
		@Value("#{stepExecutionContext['maxValue']}") String maxValue) {
		System.out.println("reading " + minValue + " to " + maxValue + "already read: " + alreadyread);
		JdbcPagingItemReader<Ratings> reader = new JdbcPagingItemReader<>();
		
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(250);
		reader.setRowMapper(new RatingsRowMapper());
		
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("tconst, averageRating, numVotes");
		queryProvider.setFromClause("from ratings");
		queryProvider.setWhereClause("where tconst >= '" + minValue + "' and tconst <= '" + maxValue + "'");
		Map<String, Order> sortKeys = new HashMap<>(1);
		
		sortKeys.put("tconst", Order.ASCENDING);
		
		queryProvider.setSortKeys(sortKeys);
		
		
		reader.setQueryProvider(queryProvider);
		
		return reader;
	}
	
	@Bean
	public ItemWriter<Ratings> writer() {
		return items -> {
			for (Ratings item : items) {
				alreadyread++;
				System.out.println(item);
			}
		};
	}
	
	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1")
			.partitioner(slaveStep().getName(), partitioner())
			.step(slaveStep())
			.partitionHandler(partitionHandler(null))
			.build();
	}
	
	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slaveStep")
			.<Ratings, Ratings>chunk(250)
			.reader(pagingItemReader(null, null))
			.writer(writer())
			.build();
	}
	
	@Bean
	@Profile("master")
	public Job job() throws Exception {
		return jobBuilderFactory.get("job1")
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.build();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
