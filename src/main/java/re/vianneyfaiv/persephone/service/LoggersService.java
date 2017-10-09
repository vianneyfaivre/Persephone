package re.vianneyfaiv.persephone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Loggers;
import re.vianneyfaiv.persephone.exception.ErrorHandler;

/**
 * Calls /loggers
 */
@Service
public class LoggersService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggersService.class);

	@Autowired
	private RestTemplate restTemplate;

	public Loggers getLoggers(Application app) {

		String url = app.endpoints().loggers();

		LOGGER.debug("GET {}", url);

		try {
			return this.restTemplate.getForObject(url, Loggers.class);
		} catch(RestClientException e) {
			throw ErrorHandler.handle(app, url, e);
		}
	}

	public void changeLevel(Application app, String loggerName, String newLevel) {

		String url = app.endpoints().loggers(loggerName);
		re.vianneyfaiv.persephone.domain.Logger body = new re.vianneyfaiv.persephone.domain.Logger(newLevel, null);

		LOGGER.debug("POST {} with configuredLevel={}", url, newLevel);

		this.restTemplate.postForEntity(url, body, String.class);
	}
}
