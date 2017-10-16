package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.env.Environment;
import re.vianneyfaiv.persephone.exception.ApplicationRuntimeException;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.service.LogsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.LOGS)
public class LogsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsPage.class);

	@Autowired
	private LogsService logsService;

	@Autowired
	private EnvironmentService envService;

	@Autowired
	private PageHelper pageHelper;

	@Value("${persephone.logs.refresh-every-x-seconds}")
	private int refreshTimeout;

	@Value("${persephone.logs.bytes-to-retrieve}")
	private long bytesToRetrieve;

	@PostConstruct
	public void init() {
		pageHelper.setLayoutStyle(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pageHelper.setErrorHandler(this);

		this.removeAllComponents();

		// Get application
		int appId = Integer.valueOf(event.getParameters());

		LOGGER.debug("Loading logs of application with id {}", appId);

		Application app = pageHelper.getApp(appId);

		// Download button
		Button downloadButton = getDownloadButton(app);

		// header
		this.addComponent(new PageHeader(app, "Logs", downloadButton));

		boolean endpointAvailable = this.logsService.endpointAvailable(app);

		/*
		 * Logs not available
		 */
		if(!endpointAvailable) {

			Environment env = this.envService.getEnvironment(app);
			String loggingPath = env.get("logging.path");
			String loggingFile = env.get("logging.file");

			String noLogsText = new StringBuilder()
									.append(String.format("Endpoint %s is not available", app.endpoints().logfile()))
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
			String logs = logsService.getLogs(app, bytesToRetrieve);

			Label logsLabel = new Label(logs, ContentMode.PREFORMATTED);
			logsLabel.setStyleName("app-logs");

			ajaxRefreshInit((args) -> logsLabel.setValue(logsService.getLogs(app, bytesToRetrieve)));

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
