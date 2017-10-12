package re.vianneyfaiv.persephone.domain;

import java.util.Map;

import org.springframework.boot.actuate.health.Status;

/**
 *
 * @see org.springframework.boot.actuate.health.Health
 */
public class Health {

	private Status status;
	private Map<String, Object> details;

	public Status getStatus() {
		return this.status;
	}

	public Map<String, Object> getDetails() {
		return this.details;
	}
}
