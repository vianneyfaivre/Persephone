package re.vianneyfaiv.persephone.domain.metrics;

/**
 * Mapper for /metrics endpoint : cache
 */
public class MetricsCache {

	private String name;
	private long size;
	private double missRatio;
	private double hitRatio;

	public MetricsCache(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public double getMissRatio() {
		return missRatio;
	}

	public double getHitRatio() {
		return hitRatio;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setMissRatio(double missRatio) {
		this.missRatio = missRatio;
	}

	public void setHitRatio(double hitRatio) {
		this.hitRatio = hitRatio;
	}

	@Override
	public String toString() {
		return "MetricsCache [name=" + name + ", size=" + size + ", missRatio=" + missRatio + ", hitRatio=" + hitRatio
				+ "]";
	}
}
