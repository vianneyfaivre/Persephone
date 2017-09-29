package re.vianneyfaiv.persephone.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.exception.PersephoneException;
import re.vianneyfaiv.persephone.exception.PersephoneTechnicalException;

/**
 * Calls /logfile
 */
@Service
public class LogsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsService.class);

	private RestTemplate restTemplate = new RestTemplate();

	public String getLogs(Application app, long bytesToRetrieve) throws PersephoneException {
		try {
			String url = String.format("%s/%s", app.getUrl(), "logfile");
			RequestEntity.HeadersBuilder request = RequestEntity.get(new URI(url));

			// get Range header value
			HttpHeaders responseHeaders = this.restTemplate.headForHeaders(new URI(url));
			long endRange = responseHeaders.getContentLength();
			long startRange = endRange - bytesToRetrieve;

			if(endRange > startRange) {
				String range = responseHeaders.get(HttpHeaders.ACCEPT_RANGES).get(0)+"="+startRange+"-"+endRange;
				LOGGER.debug("GET {} with Range {}", url, range);
				request.header(HttpHeaders.RANGE, range);
			}

			// get logs
			return this.restTemplate.exchange(request.build(), String.class).getBody();
		} catch(RestClientException | URISyntaxException e) {
			throw new PersephoneException(app, e.getMessage());
		}
	}

	public ByteArrayResource downloadLogs(Application app) {
		String url = String.format("%s/%s", app.getUrl(), "logfile");
		try {
			return this.restTemplate.getForObject(url, ByteArrayResource.class);
		} catch(RestClientException e) {
			throw new PersephoneTechnicalException(app, e.getMessage());
		}
	}
}
