package re.vianneyfaiv.persephone.domain.health;

import org.springframework.boot.actuate.health.Status;

/**
 * Mapper for /health endpoint : Database
 */
public class HealthDatabase {

	private Status status;
	private String database;
	private String hello;

	public Status getStatus() {
		return status;
	}

	public String getDatabase() {
		return database;
	}

	public String getHello() {
		return hello;
	}

	@Override
	public String toString() {
		return "HealthDatabase [status=" + status + ", database=" + database + ", hello=" + hello + "]";
	}

}
