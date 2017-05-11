package re.vianneyfaiv.persephone.domain;

import java.util.List;
import java.util.Map;

public class Environment {

	private List<String> profiles;
	private Map<String, String> systemProperties;
	private Map<String, String> systemEnvironment;

	public List<String> getProfiles() {
		return this.profiles;
	}

	public Map<String, String> getSystemProperties() {
		return this.systemProperties;
	}

	public Map<String, String> getSystemEnvironment() {
		return this.systemEnvironment;
	}
}
