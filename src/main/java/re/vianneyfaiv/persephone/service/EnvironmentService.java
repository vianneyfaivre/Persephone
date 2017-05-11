package re.vianneyfaiv.persephone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;

@Service
public class EnvironmentService {

	private RestTemplate restTemplate = new RestTemplate();

	public Environment getEnvironment(Application app) throws PersephoneServiceException {

		String url = String.format("%s/%s", app.getUrl(), "env");

		try {
			return this.restTemplate.getForObject(url, Environment.class);
		} catch(RestClientException rce) {
			throw new PersephoneServiceException(app, rce.getMessage());
		}
	}
}
