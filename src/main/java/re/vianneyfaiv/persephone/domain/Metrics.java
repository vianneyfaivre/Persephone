package re.vianneyfaiv.persephone.domain;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
		return (100 * this.memFree) / this.mem;
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

	public String getHumanReadableUptime() {
		long seconds = this.getUptime().get(ChronoUnit.SECONDS);

		if(seconds < 60) {
			return seconds+" sec";
		} else {
			long minutes = this.getUptime().toMinutes();
			if(minutes < 60) {
				return minutes+" min";
			} else {
				long hours = this.getUptime().toHours();
				if(hours < 24) {
					return hours +" hours";
				}
				else {
					long days = this.getUptime().toDays();
					return days + " days";
				}
			}
		}
	}
}
