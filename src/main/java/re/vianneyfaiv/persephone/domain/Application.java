package re.vianneyfaiv.persephone.domain;

public class Application {

	private int id;
	private String name;
	private String environment;
	private String url;
	private boolean up;

	public Application(int id, String name, String environment, String url) {
		this.id = id;
		this.name = name;
		this.environment = environment;
		this.url = url;
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

	@Override
	public String toString() {
		return "Application [id=" + this.id + ", name=" + this.name + ", environment=" + this.environment + ", url=" + this.url + ", up="
				+ this.up + "]";
	}
}
