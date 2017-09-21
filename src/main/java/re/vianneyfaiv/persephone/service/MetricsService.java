package re.vianneyfaiv.persephone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.exception.PersephoneServiceException;

/**
 * Calls /metrics
 */
@Service
public class MetricsService {

	private RestTemplate restTemplate = new RestTemplate();

	public Metrics getMetrics(Application app) throws PersephoneServiceException {

		String url = String.format("%s/%s", app.getUrl(), "metrics");

		try {
			Metrics metrics = this.restTemplate.getForObject(url, Metrics.class);

			return metrics;
		} catch(RestClientException e) {
			throw new PersephoneServiceException(app, e.getMessage());
		}
	}
}
