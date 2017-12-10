package re.vianneyfaiv.persephone.domain.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import re.vianneyfaiv.persephone.domain.env.v2.EnvironmentResponse;

/**
 * Mapper for /env endpoint
 */
public class Environment {

	private List<PropertyItem> properties;
	private Map<String, List<String>> propertiesMap;

	public Environment(EnvironmentResponse env, ActuatorVersion actuatorVersion) {
		this.properties = new ArrayList<>();

		env.getPropertySources()
			.stream()
			.forEach(ps -> {
				// get properties origin
				String baseOrigin = ps.getName();

				ps.getProperties()
					.entrySet().stream()
					.forEach(p -> {
						if(!StringUtils.isEmpty(p.getValue().getOrigin())) {
							properties.add(new PropertyItem(p.getKey(), p.getValue().getValue(), p.getValue().getOrigin()));
						} else {
							properties.add(new PropertyItem(p.getKey(), p.getValue().getValue(), baseOrigin));
						}
					});
			});

		// remove parts of origin (TMI in actuator v2)
		if(actuatorVersion == ActuatorVersion.V2) {
			this.properties.stream()
			.forEach(p -> {

				//
				if(p.getOrigin().contains(" \"")) {
					String parts[] = p.getOrigin().split(" \"");
					if(parts.length > 0) {
						p.setOrigin(parts[0]);
					}
				}
				//
				else if(p.getOrigin().contains(":")) {
					String parts[] = p.getOrigin().split(":");
					if(parts.length > 0) {
						p.setOrigin(parts[0]);
					}
				}
			});
		}

		this.propertiesMap = getPropertiesAsMap(properties);
	}

	public Environment(List<PropertyItem> properties) {
		this.properties = properties;
		this.propertiesMap = getPropertiesAsMap(properties);
	}

	private static Map<String, List<String>> getPropertiesAsMap(List<PropertyItem> properties) {
		Map<String, List<String>> propertiesMap = new HashMap<>();

		// a property can be defined in multiple files
		for(PropertyItem p : properties) {
			List<String> values = propertiesMap.get(p.getKey());
            if (values == null) {
            	values = new ArrayList<>();
				propertiesMap.put(p.getKey(), values);
			}
            values.add(p.getValue());
		}

		return propertiesMap;
	}

	public List<PropertyItem> getProperties() {
		return this.properties;
	}

	public Map<String, List<String>> getPropertiesMap() {
		return this.propertiesMap;
	}

	public List<String> getAllValues(String property) {
		return this.propertiesMap.getOrDefault(property, new ArrayList<>());
	}

	public String get(String property) {
		List<String> values = this.getAllValues(property);

		if(values.isEmpty()) {
			return "";
		} else {
			return values.get(0);
		}
	}
}
