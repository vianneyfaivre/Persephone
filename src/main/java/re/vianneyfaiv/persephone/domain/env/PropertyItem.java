package re.vianneyfaiv.persephone.domain.env;

/**
 * Mapper for a property from /env endpoint
 */
public class PropertyItem {

	private String key;
	private String value;
	private String origin;

	public PropertyItem(String key, String value, String origin) {
		this.key = key;
		this.value = value;
		this.origin = origin;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}