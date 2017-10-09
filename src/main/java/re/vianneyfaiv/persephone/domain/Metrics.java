package re.vianneyfaiv.persephone.domain;

import java.time.Duration;

public class Metrics {

	private int mem;
	private int memFree;
	private long uptime;
	private int httpSessionsActive;

	public Metrics(int mem, int memFree, long uptime, int httpSessionsActive) {
		this.mem = mem;
		this.memFree = memFree;
		this.uptime = uptime;
		this.httpSessionsActive = httpSessionsActive;
	}

	public int getMemFreePercentage() {
		return 100 - ((100 * this.memFree) / this.mem);
	}

	public Duration getUptime() {
		return Duration.ofMillis(this.uptime);
	}

	public int getMem() {
		return this.mem;
	}

	public int getMemFree() {
		return this.memFree;
	}

	public int getHttpSessionsActive() {
		return this.httpSessionsActive;
	}

	@Override
	public String toString() {
		return "Metrics [mem=" + this.mem + ", memFree=" + this.memFree + ", uptime="
				+ this.uptime + ", httpSessionsActive=" + this.httpSessionsActive + "]";
	}
}
