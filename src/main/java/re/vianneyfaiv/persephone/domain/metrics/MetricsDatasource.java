package re.vianneyfaiv.persephone.domain.metrics;

public class MetricsDatasource {

	private String name;
	private int activeConnections;
	private int connectionPoolUsage;

	public MetricsDatasource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getActiveConnections() {
		return activeConnections;
	}

	public void setActiveConnections(int activeConnections) {
		this.activeConnections = activeConnections;
	}

	public int getConnectionPoolUsage() {
		return connectionPoolUsage;
	}

	public void setConnectionPoolUsage(int connectionPoolUsage) {
		this.connectionPoolUsage = connectionPoolUsage;
	}

}
