package re.vianneyfaiv.persephone.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;

@Service
public class EnvironmentService {

	private RestTemplate restTemplate = new RestTemplate();

	public Environment getEnvironment(Application app) throws PersephoneServiceException {

		String url = String.format("%s/%s", app.getUrl(), "env");

		try {
			String json = this.restTemplate.getForEntity(url, String.class).getBody();

		    ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            JsonNode rootNode = mapper.readTree(json);

            Map<String, String> properties = new TreeMap<>();
            List<String> profiles = new ArrayList<String>();


            Iterator<JsonNode> iterator = rootNode.iterator();

            for(JsonNode profile : iterator.next()) {
            	profiles.add(profile.asText());
            }

            while(iterator.hasNext()) {
        		iterator.next().fields().forEachRemaining(p -> properties.put(p.getKey(), p.getValue().asText()));
            }

			return new Environment(profiles, properties);
		} catch(RestClientException | IOException e) {
			throw new PersephoneServiceException(app, e.getMessage());
		}
	}
}
