package re.vianneyfaiv.persephone.domain.health;

import org.springframework.boot.actuate.health.Status;

/**
 * Mapper for /health endpoint : Disk
 */
public class HealthDisk {

	private Status status;
	private long total;
	private long free;
	private long threshold;

	public Status getStatus() {
		return status;
	}

	public long getTotal() {
		return total;
	}

	public long getFree() {
		return free;
	}

	public long getThreshold() {
		return threshold;
	}

	@Override
	public String toString() {
		return "HealthDisk [status=" + status + ", total=" + total + ", free=" + free + ", threshold=" + threshold
				+ "]";
	}
}
