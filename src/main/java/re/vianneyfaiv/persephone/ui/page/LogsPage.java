package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.exception.ApplicationRuntimeException;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.service.LogsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.LOGS)
public class LogsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsPage.class);

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LogsService logsService;

	@Autowired
	private EnvironmentService envService;

	@Value("${persephone.logs.refresh-every-x-seconds}")
	private int refreshTimeout;

	@Value("${persephone.logs.bytes-to-retrieve}")
	private long bytesToRetrieve;

	@PostConstruct
	public void init() {
		// Center align layout
		this.setWidth("100%");
		this.setMargin(new MarginInfo(false, true));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Set component error handler with the one from UI.
		// This is required because when an exception is thrown when calling Navigator#navigateTo it won't be handled by UI' error handler
		setErrorHandler(getUI().getErrorHandler());

		this.removeAllComponents();

		// Get application
		int appId = Integer.valueOf(event.getParameters());

		LOGGER.debug("Loading logs of application with id {}", appId);

		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO: throw exception
		}

		// Download button
		Button downloadButton = getDownloadButton(app.get());

		// header
		this.addComponent(new PageHeader(app.get(), "Logs", downloadButton));

		boolean endpointAvailable = this.logsService.endpointAvailable(app.get());

		/*
		 * Logs not available
		 */
		if(!endpointAvailable) {

			Map<String, String> props = this.envService.getEnvironment(app.get()).getPropertiesMap();
			String loggingPath = props.get("logging.path") == null ? "<PROPERTY NOT SET>" : props.get("logging.path");
			String loggingFile = props.get("logging.file") == null ? "<PROPERTY NOT SET>" : props.get("logging.file");;

			String noLogsText = new StringBuilder()
									.append(String.format("Endpoint %s is not available", app.get().endpoints().logfile()))
									.append("\n\n")
									.append("Spring uses those properties for getting the logs:")
									.append("\n")
									.append(String.format("- logging.path=%s", loggingPath))
									.append("\n")
									.append(String.format("- logging.file=%s", loggingFile))
									.append("\n\n")
									.append("(at least one of those properties have to be properly set)")
									.toString();


			Label noLogsLabel = new Label(noLogsText, ContentMode.PREFORMATTED);
			this.addComponent(noLogsLabel);
		}
		/*
		 * Logs available
		 */
		else {
			String logs = logsService.getLogs(app.get(), bytesToRetrieve);

			Label logsLabel = new Label(logs, ContentMode.PREFORMATTED);
			logsLabel.setStyleName("app-logs");

			ajaxRefreshInit((args) -> logsLabel.setValue(logsService.getLogs(app.get(), bytesToRetrieve)));

			this.addComponent(new Label(String.format("Auto-refresh every %s seconds (last %s chars).", refreshTimeout, bytesToRetrieve)));
			this.addComponent(logsLabel);
		}
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		ajaxRefreshDestroy();
		View.super.beforeLeave(event);
	}

	private void ajaxRefreshInit(JavaScriptFunction intervalFn) {
		JavaScript.getCurrent().addFunction("Persephone.logs.refreshLogs", intervalFn);

		String js = String.format("Persephone.logs.refreshLogsInterval = setInterval(Persephone.logs.refreshLogs, %s);", refreshTimeout * 1000);
		JavaScript.getCurrent().execute(js);
	}

	private void ajaxRefreshDestroy() {
		JavaScript.getCurrent().execute("clearInterval(Persephone.logs.refreshLogsInterval)");
		JavaScript.getCurrent().removeFunction("Persephone.logs.refreshLogs");
	}

	private Button getDownloadButton(Application app) {
		// Download logs button : download file
		StreamResource resource = getLogsStream(app);

		// Download logs button
		Button downloadButton = new Button("Download full logfile");

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
				throw new ApplicationRuntimeException(app, String.format("Unable to get logs file: %s", ex.getMessage()));
			}
		}, String.format("logs-%s-%s-%s.txt", app.getName(), app.getEnvironment(), (new Date()).getTime()));
		return resource;
	}
}
