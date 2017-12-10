package re.vianneyfaiv.persephone.domain.env.v2;

import java.util.Map;

public class PropertySource {

	private String name;
	private Map<String, Property> properties;

	public String getName() {
		return name;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

}
