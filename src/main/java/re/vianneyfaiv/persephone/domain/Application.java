package re.vianneyfaiv.persephone.domain;

public class Application {

	private String url;
	private boolean up;

	public Application(String url) {
		this.url = url;
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
}
