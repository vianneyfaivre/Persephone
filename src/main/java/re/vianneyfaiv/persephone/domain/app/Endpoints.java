package re.vianneyfaiv.persephone.domain.app;

import java.util.Arrays;
import java.util.List;

public class Endpoints {

	private String baseUrl;

	public Endpoints(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<String> asList() {
		return Arrays.asList(
				this.env(),
				this.health(),
				this.logfile(),
				this.metrics(),
				this.loggers());
	}

	public String env() {
		return this.baseUrl + "/env";
	}

	public String health() {
		return this.baseUrl + "/health";
	}

	public String logfile() {
		return this.baseUrl + "/logfile";
	}

	public String metrics() {
		return this.baseUrl + "/metrics";
	}

	public String trace() {
		return this.baseUrl + "/trace";
	}

	public String loggers() {
		return this.baseUrl + "/loggers";
	}

	public String loggers(String logger) {
		return this.baseUrl + "/loggers/" + logger;
	}
}
