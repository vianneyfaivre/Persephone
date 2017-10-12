package re.vianneyfaiv.persephone.domain;

import java.util.Optional;

import org.springframework.boot.actuate.health.Status;

/**
 *
 * @see org.springframework.boot.actuate.health.Health
 */
public class Health {

	private Status status;
	private HealthDisk diskSpace;
	private HealthDatabase db;

	public Health() {
	}

	public Health(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
	}

	public Optional<HealthDisk> getDiskSpace() {
		return Optional.ofNullable(diskSpace);
	}

	public Optional<HealthDatabase> getDb() {
		return Optional.ofNullable(db);
	}
}
