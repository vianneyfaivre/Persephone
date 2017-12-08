package re.vianneyfaiv.persephone.domain.app;

import org.springframework.util.StringUtils;

import re.vianneyfaiv.persephone.domain.env.ActuatorVersion;

public class Application {

	private int id;
	private String name;
	private String environment;
	private String url;
	private boolean up; // set when application is clicked
	private Endpoints endpoints;
	private AuthScheme authScheme;
	private String actuatorUsername;
	private String actuatorPassword;
	private ActuatorVersion actuatorVersion; // set when application is clicked

	/**
	 * Application without HTTP Auth
	 */
	public Application(int id, String name, String environment, String url) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.url = url;
		this.endpoints = new Endpoints(url);
		this.authScheme = AuthScheme.NONE;
	}

	/**
	 * Application with HTTP Auth Basic enabled
	 */
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

	public ActuatorVersion getActuatorVersion() {
		return actuatorVersion;
	}

	public void setActuatorVersion(ActuatorVersion actuatorVersion) {
		this.actuatorVersion = actuatorVersion;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", environment=" + environment + ", url=" + url + ", up="
				+ up + ", authScheme=" + authScheme + "]";
	}
}
