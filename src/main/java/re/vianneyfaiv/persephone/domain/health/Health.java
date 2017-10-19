package re.vianneyfaiv.persephone.domain.health;

import java.util.Optional;

import org.springframework.boot.actuate.health.Status;

/**
 * Mapper for /health endpoint
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
