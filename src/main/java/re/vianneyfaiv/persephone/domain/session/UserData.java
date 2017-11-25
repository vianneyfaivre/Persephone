package re.vianneyfaiv.persephone.domain.session;

import re.vianneyfaiv.persephone.domain.logs.LogsRange;

/**
 * Data held in HTTP session
 */
public class UserData {

	/**
	 * GET /logs page: current range retrieved by user
	 */
	private LogsRange currentRange;

	/**
	 * GET /logs page: auto scroll to the bottom (enabled by default)
	 */
	private boolean tailAutoScrollEnabled = true;

	public LogsRange getCurrentRange() {
		return currentRange;
	}

	public void setCurrentRange(LogsRange currentRange) {
		this.currentRange = currentRange;
	}

	public void toggleTailAutoScroll() {
		if(tailAutoScrollEnabled) {
			tailAutoScrollEnabled = false;
		} else {
			tailAutoScrollEnabled = true;
		}
	}

	public boolean isTailAutoScrollEnabled() {
		return tailAutoScrollEnabled;
	}

}
