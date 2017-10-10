package re.vianneyfaiv.persephone.config;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.AuthScheme;
import re.vianneyfaiv.persephone.service.ApplicationService;

@Configuration
public class RestTemplateFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateFactory.class);

	// Spring

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ConfigurableListableBeanFactory registry;

	// Persephone RestTemplate

	@Autowired
	private DefaultRestTemplateConfig defaultRestTemplateConfig;

	@Autowired
	private ClientHttpRequestFactory requestFactory;

	@Autowired
	private RestTemplate restTemplate;

	// Persephone service

	@Autowired
	private ApplicationService applicationsService;

	public void init() {
		applicationsService
			.findAll().stream()
			.filter(app -> app.getAuthScheme() == AuthScheme.BASIC && app.isAuthValid())
			.forEach(createRestTemplateBasicAuth(registry));
	}

	public RestTemplate get(Application app) {

		String beanName = this.getRestTemplateBeanName(app);

		if(app.getAuthScheme() == AuthScheme.BASIC) {
			if(applicationContext.containsBean(beanName)) {
				LOGGER.debug("Found RestTemplate with BASIC auth for application with id {}", app.getId());
				return applicationContext.getBean(beanName, RestTemplate.class);
			} else {
				LOGGER.warn("Application with id {} has BASIC auth enabled but the RestTemplate bean has not been registered", app.getId());
			}
		}

		return restTemplate;
	}

	private Consumer<Application> createRestTemplateBasicAuth(ConfigurableListableBeanFactory registry) {
		return app -> {

			// Create rest template instance
			RestTemplate restTemplate = defaultRestTemplateConfig.restTemplate(requestFactory);

			// Configure it with BASIC auth
			restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(app.getActuatorUsername(), app.getActuatorPassword()));

			LOGGER.info("Registered RestTemplate with BASIC auth for application with id {}", app.getId());

			// Add bean in Spring application context
			registry.registerSingleton(getRestTemplateBeanName(app), restTemplate);
		};
	}

	private String getRestTemplateBeanName(Application app) {
		return "restTemplateApplication" + app.getId();
	}
}
