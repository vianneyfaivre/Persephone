package re.vianneyfaiv.persephone.ui.fragment;

import java.util.Map;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

public class PropertiesPanel extends VerticalLayout {

	public PropertiesPanel(String title, Map<String, String> properties) {

		VerticalLayout propertiesLayout = new VerticalLayout();

		properties
			.entrySet()
			.stream()
			.map(entry -> {
				Label key = new Label(entry.getKey());
				Label value = new Label(entry.getValue());

				return new HorizontalLayout(key, value);
			})
			.forEach(c -> propertiesLayout.addComponent(c));

		Panel popupContent = new Panel(propertiesLayout);
		popupContent.setHeight(400, Unit.PIXELS);
		popupContent.setWidth(800, Unit.PIXELS);

		PopupView popup = new PopupView(title, popupContent);

		this.addComponent(popup);
	}
}
