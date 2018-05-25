package re.vianneyfaiv.persephone.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.env.ActuatorVersion;
import re.vianneyfaiv.persephone.domain.env.Environment;
import re.vianneyfaiv.persephone.domain.env.PropertyItem;
import re.vianneyfaiv.persephone.domain.env.v2.EnvironmentResponse;
import re.vianneyfaiv.persephone.exception.RestTemplateErrorHandler;

/**
 * Calls /env
 */
@Service
public class EnvironmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public ActuatorVersion getActuatorVersion(Application app) {
		String url = app.endpoints().env();

		try {
			LOGGER.debug("HEAD {}", url);

			RestTemplate restTemplate = new RestTemplate(restTemplates.getForHead(app).getRequestFactory());

			HttpHeaders headers = restTemplate.headForHeaders(new URI(url));

			return ActuatorVersion.parse(headers.getContentType());
		} catch (HttpClientErrorException clientEx) {

			// Spring Boot 1.3 does not allow HEAD method, so let's assume
			// the version is V1
			if (clientEx.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
				return ActuatorVersion.V1;
			} else {
				throw RestTemplateErrorHandler.handle(app, url, clientEx);
			}

		} catch (HttpServerErrorException serverEx) {

			// If the HEAD request is not supported, a HTTP 500 could be thrown
			// so let's assume the version is V1
			if (serverEx.getStatusCode().is5xxServerError()) {
				return ActuatorVersion.V1;
			} else {
				throw RestTemplateErrorHandler.handle(app, url, serverEx);
			}

		} catch(RestClientException ex) {
			throw RestTemplateErrorHandler.handle(app, url, ex);
		} catch (URISyntaxException e) {
			throw RestTemplateErrorHandler.handle(app, e);
		}

	}

	public Environment getEnvironment(Application app) {
		switch (app.getActuatorVersion()) {
		case V2:
			return this.getEnvironmentV2(app);
		case V1:
		default:
			return this.getEnvironmentV1(app);
		}
	}

	public Environment getEnvironmentV1(Application app) {

		String url = app.endpoints().env();

		try {
			LOGGER.debug("GET {}", url);
			ResponseEntity<String> response = restTemplates.get(app).getForEntity(url, String.class);

			String json = response.getBody();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			JsonNode rootNode = mapper.readTree(json);

			List<PropertyItem> properties = new ArrayList<>();

			Iterator<Entry<String, JsonNode>> iterator = rootNode.fields();

			// Get properties
			while (iterator.hasNext()) {
				Entry<String, JsonNode> current = iterator.next();

				current.getValue().fields().forEachRemaining(
						// current.getKey ==> origin of the property : system,
						// application.properties, etc
						p -> properties.add(new PropertyItem(p.getKey(), p.getValue().asText(), current.getKey())));
			}

			return new Environment(properties);
		} catch (RestClientException ex) {
			throw RestTemplateErrorHandler.handle(app, url, ex);
		} catch (IOException e) {
			throw RestTemplateErrorHandler.handle(app, e);
		}
	}

	public Environment getEnvironmentV2(Application app) {

		String url = app.endpoints().env();

		try {
			LOGGER.debug("GET {}", url);
			ResponseEntity<EnvironmentResponse> response = restTemplates.get(app).getForEntity(url, EnvironmentResponse.class);

			return new Environment(response.getBody(), app.getActuatorVersion());
		} catch (RestClientException ex) {
			throw RestTemplateErrorHandler.handle(app, url, ex);
		}
	}

}
