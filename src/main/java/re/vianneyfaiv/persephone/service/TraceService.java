package re.vianneyfaiv.persephone.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.trace.Trace;
import re.vianneyfaiv.persephone.domain.trace.TraceInfoHeaders;
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

			ObjectMapper objectMapper = new ObjectMapper();
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addDeserializer(TraceInfoHeaders.class, new AttributeDeserializer());
			objectMapper.registerModule(simpleModule);

			LOGGER.debug("GET {}", url);
			String response = restTemplates.get(app).getForObject(url, String.class);

			Trace[] traces = objectMapper.readValue(response, Trace[].class);

			if(traces.length != 0) {
				return Arrays.asList(traces);
			} else {
				return new ArrayList<>();
			}
		} catch(RestClientException e) {
			throw RestTemplateErrorHandler.handle(app, url, e);
		} catch (IOException e) {
			throw RestTemplateErrorHandler.handle(app, e);
		}
	}

	public class AttributeDeserializer extends JsonDeserializer<TraceInfoHeaders> {

	    @Override
	    public TraceInfoHeaders deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
	    	JsonNode root = p.readValueAsTree();
	        Map<String, List<String>> request = parse(root.get("request"));
	        Map<String, List<String>> response = parse(root.get("response"));

	        return new TraceInfoHeaders(request, response);
	    }

	    private Map<String, List<String>> parse(TreeNode node) {
	    	Map<String, List<String>> result = new HashMap<>();

	    	Iterator<String> itKeys = node.fieldNames();
	    	while(itKeys.hasNext()) {
	    		String key = itKeys.next();
	    		List<String> values = new ArrayList<>();

	    		// Header has only one value
	    		if(node.get(key).isValueNode()) {
	    			values.add(((JsonNode)node.get(key)).asText());
	    		}
	    		// Header has multiple values
	    		else if (node.get(key).isArray()){
	    			for (JsonNode val : (JsonNode) node.get(key)) {
	    		        values.add(val.asText());
	    		    }
	    		}

	    		result.put(key, values);
	    	}

	    	return result;
	    }
	}
}
