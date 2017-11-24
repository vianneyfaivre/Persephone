package re.vianneyfaiv.persephone.domain.session;

import re.vianneyfaiv.persephone.domain.logs.LogsRange;

/**
 * Data held in HTTP session
 */
public class UserData {

	private LogsRange currentRange;

	public LogsRange getCurrentRange() {
		return currentRange;
	}

	public void setCurrentRange(LogsRange currentRange) {
		this.currentRange = currentRange;
	}

}
