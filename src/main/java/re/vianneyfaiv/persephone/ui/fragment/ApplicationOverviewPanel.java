package re.vianneyfaiv.persephone.ui.fragment;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.ui.PersephoneViews;

/**
 * Panel that displays overview of an application
 */
public class ApplicationOverviewPanel extends VerticalLayout implements View {

	public ApplicationOverviewPanel(Application app, Environment env, Metrics metrics) {
		this.addComponent(new Label(String.format("<h3>%s (%s)</h3>", app.getName(), app.getEnvironment()), ContentMode.HTML));

		this.addComponent(new Link(app.getUrl(), new ExternalResource(app.getUrl()), "_blank", 0, 0, BorderStyle.DEFAULT));

		this.addComponent(new Label("Spring Profiles:" + env.getProfiles().toString()));

		this.addComponent(new Label(String.format("Free mem : %s KB (%s %%)", metrics.getMemFree(), metrics.getMemFreePercentage())));

		this.addComponent(new Label(String.format("HTTP Sessions Active : %s", metrics.getHttpSessionsActive())));

		this.addComponent(new Label(String.format("Uptime : %s", metrics.getHumanReadableUptime())));

		this.addComponent(new Button("Properties", e -> getUI().getNavigator().navigateTo(PersephoneViews.PROPERTIES+"/"+app.getId())));

		this.addComponent(new Button("Show logs", e -> getUI().getNavigator().navigateTo(PersephoneViews.LOGS+"/"+app.getId())));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}

