package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.exception.PersephoneServiceException;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LogsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;

@UIScope
@SpringView(name=PersephoneViews.LOGS)
public class ApplicationLogsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogsPage.class);

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LogsService logsService;

	@PostConstruct
	public void init() {
	}

	@Override
	public void enter(ViewChangeEvent event) {

		this.removeAllComponents();

		// Get application
		int appId = Integer.valueOf(event.getParameters());

		LOGGER.debug("Loading logs of application with id {}", appId);

		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO: throw exception
		}

		// Title
		String title = String.format("<h2>%s (%s): Logs</h2>", app.get().getName(), app.get().getEnvironment());
		this.addComponent(new Label(title, ContentMode.HTML));

		// Download logs button
		Button downloadButton = new Button("Download full logfile");
		this.addComponent(downloadButton);

		// Download logs button : on click
		downloadButton.addClickListener(e -> {
			StreamResource resource = new StreamResource(() -> {
				try (InputStream logsStream = logsService.downloadLogs(app.get()).getInputStream()) {
					return logsStream;
				} catch (IOException ex) {
					throw new PersephoneServiceException(app.get(), String.format("Unable to get logs file: %s", ex.getMessage()));
				}
			}, "logs.txt");

			FileDownloader fileDownloader = new FileDownloader(resource);
			fileDownloader.setOverrideContentType(false);
			fileDownloader.extend(downloadButton);
		});

		// Back button
		this.addComponent(new Button("Back to applications list", e -> getUI().getNavigator().navigateTo(PersephoneViews.APPLICATIONS)));

		// Get logs
		String logs = logsService.getLogs(app.get());
		System.out.println("logs : "+logs.length());
		this.addComponent(new Label(logs, ContentMode.PREFORMATTED));
	}

}
