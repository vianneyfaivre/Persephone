package re.vianneyfaiv.persephone.service;

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

	@Autowired
	private RestTemplate restTemplate;

	public boolean isUp(Application app) {
		boolean up;

		String url = String.format("%s/%s", app.getUrl(), "health");

		try {
			Health health = this.restTemplate.getForObject(url, Health.class);
			up = "UP".equals(health.getStatus());
		} catch(RestClientException rce) {
			up = false;
		}

		return up;
	}
}
