package re.vianneyfaiv.persephone.ui.component.grid;

import java.util.Map;

public class MetricsGridRow {

	private String name;
	private Number value;

	public MetricsGridRow(Map.Entry<String, Number> metric) {
		this.name = metric.getKey();
		this.value = metric.getValue();
	}

	public String getName() {
		return name;
	}

	public Number getValue() {
		return value;
	}
}
