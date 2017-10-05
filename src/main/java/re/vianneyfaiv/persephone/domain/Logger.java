package re.vianneyfaiv.persephone.domain;

public class Logger {

	private String configuredLevel;
	private String effectiveLevel;

	public Logger() {
	}

	public Logger(String configuredLevel, String effectiveLevel) {
		this.configuredLevel = configuredLevel;
		this.effectiveLevel = effectiveLevel;
	}

	public String getConfiguredLevel() {
		return configuredLevel;
	}

	public String getEffectiveLevel() {
		return effectiveLevel;
	}

	@Override
	public String toString() {
		return "Logger [configuredLevel=" + configuredLevel + ", effectiveLevel=" + effectiveLevel + "]";
	}
}
