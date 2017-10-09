package re.vianneyfaiv.persephone.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.util.Formatters;

/**
 * Panel that displays overview of an application
 */
public class ApplicationOverviewPanel extends VerticalLayout implements View {

	public ApplicationOverviewPanel(Application app, Environment env, Metrics metrics) {

		Map<String, String> props = env.getPropertiesMap();
		String memFree = Formatters.readableFileSize(metrics.getMemFree() * 1000);
		String memTotal = Formatters.readableFileSize(metrics.getMem() * 1000);

		// Title
		Label titleLabel = new Label(String.format("<h3>%s (%s)</h3>", app.getName(), app.getEnvironment()), ContentMode.HTML);
		Label pidLabel = new Label(String.format("PID: %s", props.get("PID")));
		Link appLink = new Link(app.getUrl(), new ExternalResource(app.getUrl()), "_blank", 0, 0, BorderStyle.DEFAULT);

		// Overview info
		String springProfiles = props.get("spring.profiles.active") == null ? "" : props.get("spring.profiles.active");
		Label springProfilesLabel = new Label(String.format("Spring Profiles: %s", springProfiles));
		Label javaLabel = new Label(String.format("Java version %s (%s on %s)", props.get("java.version"), props.get("java.home"), props.get("os.name")));
		Label memLabel = new Label(String.format("Memory: %s / %s (%s%% free)", memFree, memTotal, metrics.getMemFreePercentage()));
		Label sessionsLabel = new Label(String.format("HTTP Sessions Active: %s", metrics.getHttpSessionsActive()));
		Label uptimeLabel = new Label(String.format("Uptime: %s", Formatters.readableDuration(metrics.getUptime())));

		// Buttons
		Button metricsButton = new Button("Metrics", e -> getUI().getNavigator().navigateTo(PersephoneViews.METRICS+"/"+app.getId()));
		Button propertiesButton = new Button("Properties", e -> getUI().getNavigator().navigateTo(PersephoneViews.PROPERTIES+"/"+app.getId()));
		Button logsButton = new Button("Show logs", e -> getUI().getNavigator().navigateTo(PersephoneViews.LOGS+"/"+app.getId()));
		Button loggersButton = new Button("Loggers config", e -> getUI().getNavigator().navigateTo(PersephoneViews.LOGGERS+"/"+app.getId()));
		Button actuatorButton = new Button("Actuator Endpoints", e -> getUI().getNavigator().navigateTo(PersephoneViews.ENDPOINTS+"/"+app.getId()));

		this.addComponent(titleLayout(titleLabel, pidLabel, appLink));
		this.addComponent(infoLayout(springProfilesLabel, javaLabel, memLabel, sessionsLabel, uptimeLabel));
		this.addComponent(buttonsLayout(metricsButton, propertiesButton, logsButton, loggersButton, actuatorButton));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private Layout titleLayout(Component... components) {

		VerticalLayout layout = new VerticalLayout(components);

		layout.setSizeFull();
		layout.setSpacing(false);
		layout.setMargin(false);

		return layout;
	}

	private Layout infoLayout(Component... components) {

		VerticalLayout layout = new VerticalLayout(components);

		layout.setSizeFull();
		layout.setSpacing(false);
		layout.setMargin(false);

		return layout;
	}

	private Layout buttonsLayout(Button... buttons) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		List<Component> components = new ArrayList<>(Arrays.asList(buttons));

		// Split components into sublists of 5 elements
		int partitionSize = 5;
		List<List<Component>> partitions = new ArrayList<>();
		for (int i = 0; i < components.size(); i += partitionSize) {
			partitions.add(components.subList(i, Math.min(i + partitionSize, components.size())));
		}

		// Create vertical layouts for each list of buttons
		for(List<Component> sublist : partitions) {
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.setMargin(false);

			sublist.stream().forEach(btn -> {
				btn.setSizeFull();
				vLayout.addComponent(btn);
			});

			layout.addComponent(vLayout);
		}

		return layout;
	}
}

