package re.vianneyfaiv.persephone.domain;

public class Application {

	private int id;
	private String name;
	private String environment;
	private String url;
	private boolean up;
	private Endpoints endpoints;

	public Application(int id, String name, String environment, String url) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.url = url;
		this.endpoints = new Endpoints(url);
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

	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", environment=" + environment + ", url=" + url + ", up="
				+ up + "]";
	}
}
