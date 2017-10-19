package re.vianneyfaiv.persephone.domain.metrics;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * Mapper for /metrics endpoint : rest controllers
 */
public class MetricsRest {

	private String name;
	private HttpStatus status;
	private long value;
	private int lastResponseTime;

	public MetricsRest(Map.Entry<String,Number> metric) {
		String[] parts = metric.getKey().split("\\.");

		// length == min 4 parts (a metric name can have several dots)
		if(parts.length >= 4) {
			try {
				// parts[2] == http status code
				this.status = HttpStatus.valueOf(Integer.valueOf(parts[2]));

				if(parts.length - 1 == 3) {
					// metric name has no dots, so take only parts[3]
					this.name = parts[3];
				} else {
					// parts[3]..parts[length-1] == http status code
					this.name = String.join(".", Arrays.copyOfRange(parts, 3, parts.length));
				}
				this.value = metric.getValue().longValue();
			} catch (IllegalArgumentException ignored) {
			}
		}
	}

	public boolean valid() {
		return name != null && status != null;
	}

	public String getName() {
		return name;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public long getValue() {
		return value;
	}

	public int getLastResponseTime() {
		return lastResponseTime;
	}

	public void setLastResponseTime(int lastResponseTime) {
		this.lastResponseTime = lastResponseTime;
	}

}
