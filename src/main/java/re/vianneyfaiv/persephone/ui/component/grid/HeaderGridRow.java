package re.vianneyfaiv.persephone.ui.component.grid;

import java.util.List;
import java.util.Map;

public class HeaderGridRow {

	private String header;
	private String values;

	public HeaderGridRow(Map.Entry<String, List<String>> header) {
		this.header = header.getKey();

		this.values = header.getValue().stream()
										.map(v -> v+"\n")
										.reduce("", String::concat);
	}

	public String getHeader() {
		return header;
	}

	public String getValues() {
		return values;
	}

}
