package re.vianneyfaiv.persephone.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Environment {

	private List<String> profiles;
	private List<PropertyItem> properties;

	public Environment(List<String> profiles, List<PropertyItem> properties) {
		this.profiles = profiles;
		this.properties = properties;
	}

	public List<String> getProfiles() {
		return this.profiles;
	}

	public List<PropertyItem> getProperties() {
		return this.properties;
	}

	public Map<String, String> getPropertiesMap() {
		return this.properties.stream().collect(Collectors.toMap(PropertyItem::getKey, PropertyItem::getValue));
	}
}
