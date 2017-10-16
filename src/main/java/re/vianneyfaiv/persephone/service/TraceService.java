package re.vianneyfaiv.persephone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.trace.Traces;
import re.vianneyfaiv.persephone.exception.ErrorHandler;

/**
 * Calls /trace
 */
@Service
public class TraceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public Traces getTraces(Application app) {

		String url = app.endpoints().trace();

		try {
			LOGGER.debug("GET {}", url);
			return restTemplates.get(app).getForObject(url, Traces.class);
		} catch(RestClientException e) {
			throw ErrorHandler.handle(app, url, e);
		}
	}
}
