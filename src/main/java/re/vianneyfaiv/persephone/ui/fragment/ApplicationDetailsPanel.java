package re.vianneyfaiv.persephone.ui.fragment;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Environment;

public class ApplicationDetailsPanel extends VerticalLayout implements View {

	public ApplicationDetailsPanel(Environment env) {
		this.addComponent(new Label("Profiles:" + env.getProfiles().toString()));
		this.addComponent(new PropertiesPopup("Properties", env.getProperties()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}

