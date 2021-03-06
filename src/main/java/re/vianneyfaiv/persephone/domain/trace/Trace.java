package re.vianneyfaiv.persephone.domain.trace;

/**
 * Mapper for /trace endpoint
 */
public class Trace {

	private long timestamp;
	private TraceInfo info;

	public long getTimestamp() {
		return timestamp;
	}

	public TraceInfo getInfo() {
		return info;
	}

	@Override
	public String toString() {
		return "Trace [timestamp=" + timestamp + ", info=" + info + "]";
	}
}
