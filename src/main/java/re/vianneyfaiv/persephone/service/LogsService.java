package re.vianneyfaiv.persephone.service;

import org.springframework.core.io.ByteArrayResource;
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

	private RestTemplate restTemplate = new RestTemplate();

	public String getLogs(Application app) throws PersephoneException {
		String url = String.format("%s/%s", app.getUrl(), "logfile");
		try {
			return this.restTemplate.getForObject(url, String.class);
		} catch(RestClientException e) {
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
