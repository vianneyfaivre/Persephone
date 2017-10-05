package re.vianneyfaiv.persephone.ui.component;

import java.util.Map;

import re.vianneyfaiv.persephone.domain.Logger;

public class LoggerGridRow {

	private String name;
	private String level;

	public LoggerGridRow(Map.Entry<String, Logger> logger) {
		this.name = logger.getKey();
		this.level = logger.getValue().getEffectiveLevel();
	}

	public String getName() {
		return name;
	}

	public String getLevel() {
		return level;
	}
}