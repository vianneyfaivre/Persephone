package re.vianneyfaiv.persephone.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.PropertyItem;
import re.vianneyfaiv.persephone.exception.PersephoneTechnicalException;

/**
 * Calls /env
 */
@Service
public class EnvironmentService {

	@Autowired
	private RestTemplate restTemplate;

	public Environment getEnvironment(Application app) throws PersephoneTechnicalException {

		String url = app.endpoints().env();

		try {
			String json = this.restTemplate.getForEntity(url, String.class).getBody();

		    ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            JsonNode rootNode = mapper.readTree(json);

            List<PropertyItem> properties = new ArrayList<>();
            List<String> profiles = new ArrayList<String>();

            Iterator<Entry<String, JsonNode>> iterator = rootNode.fields();

            // Get Spring Profiles
            for(JsonNode profile : iterator.next().getValue()) {
            	profiles.add(profile.asText());
            }

            // Get properties
            while(iterator.hasNext()) {
            	Entry<String, JsonNode> current = iterator.next();

            	current.getValue()
        			.fields()
        			.forEachRemaining(
        				p -> properties.add(new PropertyItem(p.getKey(), p.getValue().asText(), current.getKey()))
					);
            }

			return new Environment(profiles, properties);
		} catch(RestClientException | IOException e) {
			throw new PersephoneTechnicalException(app, e.getMessage());
		}
	}
}
