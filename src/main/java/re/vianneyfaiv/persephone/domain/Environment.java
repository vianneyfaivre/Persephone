package re.vianneyfaiv.persephone.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Environment {

	private List<PropertyItem> properties;

	public Environment(List<PropertyItem> properties) {
		this.properties = properties;
	}

	public List<PropertyItem> getProperties() {
		return this.properties;
	}

	public Map<String, String> getPropertiesMap() {
		return this.properties.stream().collect(Collectors.toMap(PropertyItem::getKey, PropertyItem::getValue));
	}
}
