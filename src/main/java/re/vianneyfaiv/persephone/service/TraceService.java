package re.vianneyfaiv.persephone.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.trace.Trace;
import re.vianneyfaiv.persephone.exception.RestTemplateErrorHandler;

/**
 * Calls /trace
 */
@Service
public class TraceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public List<Trace> getTraces(Application app) {

		String url = app.endpoints().trace();

		try {
			LOGGER.debug("GET {}", url);
			Trace[] traces = restTemplates.get(app).getForObject(url, Trace[].class);

			if(traces.length != 0) {
				return Arrays.asList(traces);
			} else {
				return new ArrayList<>();
			}
		} catch(RestClientException e) {
			throw RestTemplateErrorHandler.handle(app, url, e);
		}
	}
}
