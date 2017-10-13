package re.vianneyfaiv.persephone.ui.component;

import java.text.DecimalFormat;

import re.vianneyfaiv.persephone.domain.MetricsCache;

public class MetricsCacheGridRow {

	private String name;
	private long size;
	private String hit;
	private String miss;

	public MetricsCacheGridRow(MetricsCache metric) {
		this.name = metric.getName();
		this.size = metric.getSize();
		
		DecimalFormat format = new DecimalFormat("#.## %");
		this.hit = format.format(metric.getHitRatio());
		this.miss = format.format(metric.getMissRatio());
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public String getHit() {
		return hit;
	}

	public String getMiss() {
		return miss;
	}
}
