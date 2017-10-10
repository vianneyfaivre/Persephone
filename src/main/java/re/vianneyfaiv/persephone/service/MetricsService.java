package re.vianneyfaiv.persephone.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.exception.ErrorHandler;

/**
 * Calls /metrics
 */
@Service
public class MetricsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public Map<String, Number> getAllMetrics(Application app) {

		String url = app.endpoints().metrics();

		try {
			LOGGER.debug("GET {}", url);
			Map<String, Number> metrics = restTemplates.get(app).getForObject(url, Map.class);

			return metrics;
		} catch(RestClientException e) {
			throw ErrorHandler.handle(app, url, e);
		}
	}

	public Metrics getMetrics(Application app) {

		Map<String, Number> allMetrics = this.getAllMetrics(app);

		int mem = allMetrics.get("mem").intValue();
		int memFree = allMetrics.get("mem.free").intValue();
		long uptime = allMetrics.get("uptime").longValue();
		int httpSessionsActive = allMetrics.get("httpsessions.active").intValue();

		return new Metrics(mem, memFree, uptime, httpSessionsActive);
	}
}
