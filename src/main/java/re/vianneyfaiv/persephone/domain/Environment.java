package re.vianneyfaiv.persephone.domain;

import java.util.List;
import java.util.Map;

public class Environment {

	private List<String> profiles;
	private Map<String, String> properties;

	public Environment(List<String> profiles, Map<String, String> properties) {
		this.profiles = profiles;
		this.properties = properties;
	}

	public List<String> getProfiles() {
		return this.profiles;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}
}
