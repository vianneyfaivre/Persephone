package re.vianneyfaiv.persephone.domain;

import java.util.List;

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
}
