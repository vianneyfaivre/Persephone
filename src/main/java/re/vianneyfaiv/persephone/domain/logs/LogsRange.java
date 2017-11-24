package re.vianneyfaiv.persephone.domain.logs;

import org.springframework.http.HttpHeaders;

/**
 * Class used for generating HTTP Header "Range"
 */
public class LogsRange {

	private long start;
	private long end;

	public LogsRange(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public boolean isValid() {
		return start >= end;
	}

	/**
	 * @return the value for header {@link HttpHeaders#RANGE}
	 */
	public String toHttpHeader() {
		return String.format("bytes=%s-%s", start, end);
	}
}
