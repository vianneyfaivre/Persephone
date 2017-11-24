package re.vianneyfaiv.persephone.domain.logs;

import org.springframework.http.HttpHeaders;

public class LogsRange {

	private long min;
	private long max;

	public LogsRange(long min, long max) {
		this.min = min;
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	/**
	 * @return the value for header {@link HttpHeaders#RANGE}
	 */
	public String toHttpHeader() {
		return String.format("bytes=%s-%s", min, max);
	}
}
