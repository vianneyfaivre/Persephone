package re.vianneyfaiv.persephone.domain.app;

import java.util.Arrays;
import java.util.List;

/**
 * List of Actuator endpoints supported by Persephone
 */
public class Endpoints {

	private String baseUrl;

	public Endpoints(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<String> asList() {
		return Arrays.asList(
				this.actuator(), this.auditevents(), this.autoconfig(), this.beans(),
				this.configprops(), this.docs(), this.dump(), this.env(), this.flyway(),
				this.health(), this.heapdump(), this.jolokia(), this.liquibase(),
				this.logfile(), this.loggers(), this.mappings(), this.metrics(),
				this.trace()
		);
	}

	public String actuator() {
		return this.baseUrl + "/actuator";
	}

	public String auditevents() {
		return this.baseUrl + "/auditevents";
	}

	public String autoconfig() {
		return this.baseUrl + "/autoconfig";
	}

	public String beans() {
		return this.baseUrl + "/beans";
	}

	public String configprops() {
		return this.baseUrl + "/configprops";
	}

	public String docs() {
		return this.baseUrl + "/docs";
	}

	public String dump() {
		return this.baseUrl + "/dump";
	}

	public String env() {
		return this.baseUrl + "/env";
	}

	public String flyway() {
		return this.baseUrl + "/flyway";
	}

	public String health() {
		return this.baseUrl + "/health";
	}

	public String heapdump() {
		return this.baseUrl + "/heapdump";
	}

	public String jolokia() {
		return this.baseUrl + "/jolokia";
	}

	public String liquibase() {
		return this.baseUrl + "/liquibase";
	}

	public String logfile() {
		return this.baseUrl + "/logfile";
	}

	public String loggers() {
		return this.baseUrl + "/loggers";
	}

	public String loggers(String logger) {
		return this.baseUrl + "/loggers/" + logger;
	}

	public String mappings() {
		return this.baseUrl + "/mappings";
	}

	public String metrics() {
		return this.baseUrl + "/metrics";
	}

	public String shutdown() {
		return this.baseUrl + "/shutdown";
	}

	public String trace() {
		return this.baseUrl + "/trace";
	}
}
