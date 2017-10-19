package re.vianneyfaiv.persephone.domain.logger;

import java.util.List;
import java.util.Map;

/**
 * Mapper for /loggers endpoint
 */
public class Loggers {

	private List<String> levels;
	private Map<String, Logger> loggers;

	public List<String> getLevels() {
		return levels;
	}

	public Map<String, Logger> getLoggers() {
		return loggers;
	}
}
