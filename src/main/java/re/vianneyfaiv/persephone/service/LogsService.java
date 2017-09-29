package re.vianneyfaiv.persephone.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;

/**
 * Calls /logfile
 */
@Service
public class LogsService {

	private RestTemplate restTemplate = new RestTemplate();

	public String getLogs(Application app) {
		String url = String.format("%s/%s", app.getUrl(), "logfile");
		return this.restTemplate.getForObject(url, String.class);
	}

	public ByteArrayResource downloadLogs(Application app) {
		String url = String.format("%s/%s", app.getUrl(), "logfile");
		return this.restTemplate.getForObject(url, ByteArrayResource.class);
	}
}
