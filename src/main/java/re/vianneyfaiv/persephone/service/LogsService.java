package re.vianneyfaiv.persephone.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.exception.RestTemplateErrorHandler;

/**
 * Calls /logfile
 */
@Service
public class LogsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public boolean endpointAvailable(Application app) {

		String url = app.endpoints().logfile();

		try {
			LOGGER.debug("HEAD {}", url);
			restTemplates.get(app).headForHeaders(new URI(url));
			return true;
		} catch (RestClientException | URISyntaxException e) {
			LOGGER.warn("Application {} endpoint {} not reachable: {}", app.getId(), url, e.getMessage());
			return false;
		}
	}

	public String getLogs(Application app, long bytesToRetrieve) {
		String url = app.endpoints().logfile();

		try {
			RequestEntity.HeadersBuilder<?> request = RequestEntity.get(new URI(url));

			// get Range header value
			HttpHeaders responseHeaders = restTemplates.get(app).headForHeaders(new URI(url));
			long endRange = responseHeaders.getContentLength();

			long startRange = endRange - bytesToRetrieve;
			startRange = startRange <= 0 ? 0 : startRange;

			if(endRange > startRange) {
				String range = responseHeaders.get(HttpHeaders.ACCEPT_RANGES).get(0)+"="+startRange+"-"+endRange;
				LOGGER.debug("GET {} with Range {}", url, range);
				request.header(HttpHeaders.RANGE, range);
			}

			// get logs
			return restTemplates.get(app).exchange(request.build(), String.class).getBody();
		} catch(RestClientException e) {
			throw RestTemplateErrorHandler.handle(app, url, e);
		} catch (URISyntaxException e) {
			throw RestTemplateErrorHandler.handle(app, e);
		}
	}

	public ByteArrayResource downloadLogs(Application app) {
		String url = app.endpoints().logfile();
		try {
			LOGGER.debug("GET {}", url);
			return restTemplates.get(app).getForObject(url, ByteArrayResource.class);
		} catch(RestClientException e) {
			throw RestTemplateErrorHandler.handle(app, url, e);
		}
	}
}
