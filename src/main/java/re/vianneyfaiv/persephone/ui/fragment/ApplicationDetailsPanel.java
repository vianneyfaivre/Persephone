package re.vianneyfaiv.persephone.ui.fragment;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.Metrics;

/**
 * Panel that displays an application details
 */
public class ApplicationDetailsPanel extends VerticalLayout implements View {

	public ApplicationDetailsPanel(Application app, Environment env, Metrics metrics) {
		this.addComponent(new Label(String.format("<h2>%s (%s)</h2>", app.getName(), app.getEnvironment()), ContentMode.HTML));

		this.addComponent(new Label("Profiles:" + env.getProfiles().toString()));

		this.addComponent(new Label(String.format("Free mem : %s (%s %%)", metrics.getMemFree(), metrics.getMemFreePercentage())));

		this.addComponent(new Label(String.format("HTTP Sessions Active : %s", metrics.getHttpSessionsActive())));

		this.addComponent(new Label(String.format("Uptime : %s", metrics.getHumanReadableUptime())));

		this.addComponent(new PropertiesPopup("Properties", env.getProperties()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}

