package re.vianneyfaiv.persephone.ui.fragment;

import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

public class PropertiesPopup extends VerticalLayout {

	public PropertiesPopup(String title, Map<String, String> properties) {

		// Popup title and close button
		Label popupTitle = new Label(title);
		Button closeButton = new Button("Close");
		HorizontalLayout titleBar = new HorizontalLayout(popupTitle, closeButton);

		// Popup grid
		Grid<PropertyItem> grid = new Grid<>(PropertyItem.class);
		grid.setColumns("key", "value");
		grid.setItems(properties.entrySet().stream().map(e -> new PropertyItem(e.getKey(), e.getValue())));

		// Create Popup
		VerticalLayout popupContent = new VerticalLayout(titleBar, grid);

		PopupView popup = new PopupView(title, popupContent);
		popup.setCaption(title);
		popup.setHideOnMouseOut(false);

		closeButton.addListener(e -> popup.setPopupVisible(false));

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
