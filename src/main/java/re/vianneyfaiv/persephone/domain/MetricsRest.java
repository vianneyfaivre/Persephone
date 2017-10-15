package re.vianneyfaiv.persephone.domain;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class MetricsRest {

	private String name;
	private HttpStatus status;
	private long value;
	private int lastResponseTime;

	public MetricsRest(Map.Entry<String,Number> metric) {
		String[] parts = metric.getKey().split("\\.");

		if(parts.length >= 4) {
			try {
				this.status = HttpStatus.valueOf(Integer.valueOf(parts[2]));

				if(parts.length - 1 == 3) {
					this.name = parts[3];
				} else {
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
