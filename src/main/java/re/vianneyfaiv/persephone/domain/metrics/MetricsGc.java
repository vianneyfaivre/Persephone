package re.vianneyfaiv.persephone.domain.metrics;

import java.time.Duration;

/**
 * Mapper for /metrics endpoint : Garbage Collection
 */
public class MetricsGc {

	private String name;
	private int count;
	private Duration time;

	public MetricsGc(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public Duration getTime() {
		return time;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

}
