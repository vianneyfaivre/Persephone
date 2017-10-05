package re.vianneyfaiv.persephone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.exception.PersephoneTechnicalException;

/**
 * Calls /metrics
 */
@Service
public class MetricsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

	@Autowired
	private RestTemplate restTemplate;

	public Metrics getMetrics(Application app) throws PersephoneTechnicalException {

		String url = app.endpoints().metrics();

		try {
			LOGGER.debug("GET {}", url);
			Metrics metrics = this.restTemplate.getForObject(url, Metrics.class);

			return metrics;
		} catch(RestClientException e) {
			throw new PersephoneTechnicalException(app, e.getMessage());
		}
	}
}
