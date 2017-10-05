package re.vianneyfaiv.persephone.domain;

public class Endpoints {

	private String baseUrl;

	public Endpoints(String baseUrl) {
		this.baseUrl = baseUrl;
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
}
