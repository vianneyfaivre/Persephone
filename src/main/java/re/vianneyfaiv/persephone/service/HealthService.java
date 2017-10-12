package re.vianneyfaiv.persephone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Health;

/**
 * Calls /health
 */
@Service
public class HealthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public boolean isUp(Application app) {
		return this.getHealth(app).getStatus() == Status.UP;
	}

	public Health getHealth(Application app) {
		String url = app.endpoints().health();

		try {
			LOGGER.debug("GET {}", url);
			return restTemplates.get(app).getForObject(url, Health.class);
		} catch(RestClientException rce) {
			return new Health(Status.DOWN);
		}
	}
}
