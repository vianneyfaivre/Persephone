package re.vianneyfaiv.persephone.domain.env.v2;

import java.util.List;

public class EnvironmentResponse {

	private List<String> activeProfiles;
	private List<PropertySource> propertySources;

	public List<String> getActiveProfiles() {
		return activeProfiles;
	}

	public List<PropertySource> getPropertySources() {
		return propertySources;
	}
}
