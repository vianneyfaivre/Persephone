package re.vianneyfaiv.persephone.domain.app;

import org.springframework.util.StringUtils;

public class Application {

	private int id;
	private String name;
	private String environment;
	private String url;
	private boolean up;
	private Endpoints endpoints;
	private AuthScheme authScheme;
	private String actuatorUsername;
	private String actuatorPassword;

	public Application(int id, String name, String environment, String url) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.url = url;
		this.endpoints = new Endpoints(url);
		this.authScheme = AuthScheme.NONE;
	}

	public Application(int id, String name, String environment, String url, String username, String password) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.url = url;
		this.endpoints = new Endpoints(url);
		this.authScheme = AuthScheme.BASIC;
		this.actuatorUsername = username;
		this.actuatorPassword = password;
	}

	public boolean isAuthValid() {
		if(AuthScheme.BASIC == authScheme) {
			return !StringUtils.isEmpty(actuatorUsername) && !StringUtils.isEmpty(actuatorPassword);
		}

		return true;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEnvironment() {
		return this.environment;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isUp() {
		return this.up;
	}

	public Endpoints endpoints() {
		return this.endpoints;
	}

	public AuthScheme getAuthScheme() {
		return authScheme;
	}

	public String getActuatorUsername() {
		return actuatorUsername;
	}

	public String getActuatorPassword() {
		return actuatorPassword;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", environment=" + environment + ", url=" + url + ", up="
				+ up + ", authScheme=" + authScheme + "]";
	}
}
