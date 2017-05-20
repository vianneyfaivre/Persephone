package re.vianneyfaiv.persephone.ui.fragment;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.PropertyItem;

public class PropertiesPopup extends VerticalLayout {

	public PropertiesPopup(String title, List<PropertyItem> properties) {

		// Popup title and close button
		Label popupTitle = new Label(title);
		Button closeButton = new Button("Close");
		HorizontalLayout titleBar = new HorizontalLayout(popupTitle, closeButton);

		// Popup grid
		Grid<PropertyItem> grid = new Grid<>(PropertyItem.class);
		grid.setColumns("key", "value", "origin");
		grid.setItems(properties);

		// Create Popup
		VerticalLayout popupContent = new VerticalLayout(titleBar, grid);

		PopupView popup = new PopupView(title, popupContent);
		popup.setCaption(title);
		popup.setHideOnMouseOut(false);

		closeButton.addListener(e -> popup.setPopupVisible(false));

		this.addComponent(popup);
	}
}
