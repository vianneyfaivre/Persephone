package re.vianneyfaiv.persephone.bootstrap;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.AuthScheme;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.HealthService;

/**
 * Batch ran at startup. Reads {@link Application}s from a CSV file and load them in memory
 */
@Configuration
@EnableBatchProcessing
public class BatchReadApplicationsFromCsv {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchReadApplicationsFromCsv.class);

	@Value("${persephone.applications.csv}")
	private String csvPath;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private HealthService healthService;

	@Autowired
	private RestTemplateFactory restTemplateFactory;

	@Bean
	public FlatFileItemReader<Application> reader() {
		FlatFileItemReader<Application> reader = new FlatFileItemReader<>();

		LOGGER.info("Reading applications from file {}", this.csvPath);

		reader.setResource(new PathResource(this.csvPath));
		reader.setLinesToSkip(1);

		// counter for Application#id
		final AtomicInteger counter = new AtomicInteger(1);

		reader.setLineMapper(new DefaultLineMapper<Application>() {
			{
				// define the name of each line token
				this.setLineTokenizer(new DelimitedLineTokenizer() {
					{
						this.setNames(new String[] { "name", "environment", "url", "authScheme", "actuatorUsername", "actuatorPassword" });
					}
				});

				// For each line
				this.setFieldSetMapper(line -> {

					AuthScheme authScheme = AuthScheme.parse(line.readString("authScheme"));
					Application app;

					if(authScheme == AuthScheme.BASIC) {
						app = new Application(
								counter.getAndIncrement(),
								line.readString("name"),
								line.readString("environment"),
								line.readString("url"),
								line.readString("actuatorUsername"),
								line.readString("actuatorPassword"));
					} else {
						app = new Application(
								counter.getAndIncrement(),
								line.readString("name"),
								line.readString("environment"),
								line.readString("url"));
					}

					LOGGER.info("Loaded {}", app);

					return app;
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemWriter<Application> writer() {
		return items -> this.applicationService.addApplications((List<Application>) items);
	}

	@Bean
	public Job importAppsJob() {
		return this.jobBuilderFactory
					.get("importAppsJob")
					.incrementer(new RunIdIncrementer())
					.flow(this.step1())
					.end()
					.listener(new JobExecutionListener() {
						@Override
						public void beforeJob(JobExecution jobExecution) {
						}

						/**
						 * Once the job is over, call /health on each application
						 */
						@Override
						public void afterJob(JobExecution jobExecution) {

							LOGGER.debug("Applications have been loaded. Now checking if they are up");

							// Create RestTemplate with BASIC auth (if enabled)
							restTemplateFactory.init();

							// Refresh applications health
							BatchReadApplicationsFromCsv.this.applicationService
									.findAll()
									.stream()
									.forEach(app -> app.setUp(BatchReadApplicationsFromCsv.this.healthService.isUp(app)));
						}
					})
					.build();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory
				.get("step1")
				.<Application, Application>chunk(10)
				.reader(this.reader())
				.processor(new PassThroughItemProcessor<>())
				.writer(this.writer())
				.build();
	}

	@Bean
	public ResourcelessTransactionManager transactionManager() {
	    return new ResourcelessTransactionManager();
	}

	@Bean
	public JobRepository jobRepository(ResourcelessTransactionManager transactionManager) throws Exception {
	    MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
	    mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
	    return mapJobRepositoryFactoryBean.getObject();
	}

	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
	    SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
	    simpleJobLauncher.setJobRepository(jobRepository);
	    return simpleJobLauncher;
	}

}
