package re.vianneyfaiv.persephone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Health;

/**
 * Calls /health
 */
@Service
public class HealthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthService.class);

	@Autowired
	private RestTemplate restTemplate;

	public boolean isUp(Application app) {
		boolean up;

		String url = app.endpoints().health();

		try {
			LOGGER.debug("GET {}", url);

			Health health = this.restTemplate.getForObject(url, Health.class);
			up = "UP".equals(health.getStatus());
		} catch(RestClientException rce) {
			up = false;
		}

		return up;
	}
}
