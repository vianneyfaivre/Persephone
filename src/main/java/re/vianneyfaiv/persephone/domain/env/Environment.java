package re.vianneyfaiv.persephone.domain.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for /env endpoint
 */
public class Environment {

	private List<PropertyItem> properties;
	private Map<String, List<String>> propertiesMap;

	public Environment(List<PropertyItem> properties) {
		this.properties = properties;
		this.propertiesMap = new HashMap<>();

		// a property can be defined in multiple files
		for(PropertyItem p : properties) {
			List<String> values = this.propertiesMap.get(p.getKey());
            if (values == null) {
            	values = new ArrayList<>();
				this.propertiesMap.put(p.getKey(), values);
			}
            values.add(p.getValue());
		}
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
