package re.vianneyfaiv.persephone.ui.fragment;

import java.util.Map;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

public class PropertiesPopup extends VerticalLayout {

	public PropertiesPopup(String title, Map<String, String> properties) {

		Grid<PropertyItem> grid = new Grid<>(PropertyItem.class);

		grid.setColumns("key", "value");
		grid.setItems(properties.entrySet().stream().map(e -> new PropertyItem(e.getKey(), e.getValue())));

		Panel popupContent = new Panel(grid);

		PopupView popup = new PopupView(title, popupContent);

		this.addComponent(popup);
	}

	public static class PropertyItem {
		private String key;
		private String value;

		public PropertyItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}
	}
}
