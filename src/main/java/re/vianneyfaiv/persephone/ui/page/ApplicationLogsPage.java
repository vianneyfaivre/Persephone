package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.exception.PersephoneException;
import re.vianneyfaiv.persephone.exception.PersephoneTechnicalException;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LogsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.ButtonBar;
import re.vianneyfaiv.persephone.ui.component.PageTitle;

@UIScope
@SpringView(name=PersephoneViews.LOGS)
public class ApplicationLogsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogsPage.class);

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LogsService logsService;

	@Value("${persephone.logs.refresh-every-x-seconds}")
	private int refreshTimeout;

	@Value("${persephone.logs.bytes-to-retrieve}")
	private long bytesToRetrieve;

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
		this.addComponent(new PageTitle(app.get(), "Logs"));

		// Add button bar
		Button downloadButton = getDownloadButton(app.get());
		this.addComponent(new ButtonBar(downloadButton));

		boolean endpointAvailable = this.logsService.endpointAvailable(app.get());

		/*
		 * Logs not available
		 */
		if(!endpointAvailable) {
			String noLogsText = new StringBuilder()
									.append(String.format("Endpoint %s/logfile is not available", app.get().getUrl()))
									.append("\n")
									.append("Verify that the values of properties 'logging.path' and/or 'logging.file' are correctly set")
									// TODO : display the values of properties logging.path and logging.file
									.toString();


			Label noLogsLabel = new Label(noLogsText, ContentMode.PREFORMATTED);
			this.addComponent(noLogsLabel);
		}
		/*
		 * Logs available
		 */
		else {
			// Get logs
			String logs = getLogs(app.get());

			// Display logs as plain text
			Label logsLabel = new Label(logs, ContentMode.PREFORMATTED);

			// Refresh logs every "refreshTimeout" seconds
			this.addComponent(new Label(String.format("Auto-refresh every %s seconds", refreshTimeout)));
			JavaScript.getCurrent().addFunction("refreshLogs", (args) -> logsLabel.setValue(getLogs(app.get())));
			JavaScript.getCurrent().execute(String.format("setInterval(function(){refreshLogs();},%s);", refreshTimeout * 1000));

			this.addComponent(logsLabel);
		}
	}

	private Button getDownloadButton(Application app) {
		// Download logs button : download file
		StreamResource resource = getLogsStream(app);

		// Download logs button
		Button downloadButton = new Button("Download full logfile");
		this.addComponent(downloadButton);

		FileDownloader fileDownloader = new FileDownloader(resource);
		fileDownloader.setOverrideContentType(false);
		fileDownloader.extend(downloadButton);

		// Download logs button : on click
		downloadButton.addClickListener(e -> {
			fileDownloader.setFileDownloadResource(getLogsStream(app));
		});
		return downloadButton;
	}

	private StreamResource getLogsStream(Application app) {
		StreamResource resource = new StreamResource(() -> {
			try (InputStream logsStream = logsService.downloadLogs(app).getInputStream()) {
				return logsStream;
			} catch (IOException ex) {
				throw new PersephoneTechnicalException(app, String.format("Unable to get logs file: %s", ex.getMessage()));
			}
		}, String.format("logs-%s.txt", (new Date()).getTime()));
		return resource;
	}

	private String getLogs(Application app) {
		String logs = "";
		try {
			logs = logsService.getLogs(app, bytesToRetrieve);
		} catch (PersephoneException e1) {
			LOGGER.warn(String.format("Unable to get logs for application id=%s : %s", app.getId(), e1.getMessage()));
		}
		return logs;
	}

}
