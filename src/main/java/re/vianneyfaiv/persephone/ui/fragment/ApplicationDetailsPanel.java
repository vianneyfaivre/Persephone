package re.vianneyfaiv.persephone.ui.fragment;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Environment;

public class ApplicationDetailsPanel extends VerticalLayout implements View {

	public ApplicationDetailsPanel(Environment env) {
		this.addComponent(new Label("Profiles:" + env.getProfiles().toString()));
		this.addComponent(new PropertiesPanel("System Environment", env.getSystemEnvironment()));
		this.addComponent(new PropertiesPanel("System Properties", env.getSystemProperties()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}

