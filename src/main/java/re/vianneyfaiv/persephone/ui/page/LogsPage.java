package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

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
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.env.Environment;
import re.vianneyfaiv.persephone.domain.logs.LogsRange;
import re.vianneyfaiv.persephone.domain.session.UserData;
import re.vianneyfaiv.persephone.exception.ApplicationRuntimeException;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.service.LogsService;
import re.vianneyfaiv.persephone.ui.PersephoneUI;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

/**
 * Page that display applications logs. Logs can be downloaded as a text file.
 */
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
	private double refreshTimeout;

	@Value("${persephone.logs.bytes-to-retrieve.init}")
	private long bytesToRetrieveInit;

	@Value("${persephone.logs.bytes-to-retrieve.refresh}")
	private long bytesToRetrieveRefresh;

	@Value("${persephone.logs.bytes-to-display.max}")
	private int bytesToDisplayMax;

	@PostConstruct
	public void init() {
		pageHelper.setLayoutStyle(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pageHelper.setErrorHandler(this);

		this.removeAllComponents();

		// Get application
		int appId = Integer.parseInt(event.getParameters());

		LOGGER.debug("Loading logs of application with id {}", appId);

		Application app = pageHelper.getApp(appId);

		// Check logs required properties
		boolean endpointAvailable = this.logsService.endpointAvailable(app);
		Environment env = this.envService.getEnvironment(app);
		String loggingPath = env.get("logging.path");
		String loggingFile = env.get("logging.file");

		/*
		 * Logs not available
		 */
		if(!endpointAvailable) {
			this.addComponent(new PageHeader(app, "Logs"));
			this.addComponent(getNoLogsText(app, env, loggingPath, loggingFile));
		}
		/*
		 * Logs available
		 */
		else {
			// Page Header
			Button autoScrollButton = getAutoScrollButton();

			// Get range
			LogsRange logsRange = logsService.getLastLogsRange(app, bytesToRetrieveInit);

			// Put range into user session
			UserData sessionUserData = ((PersephoneUI)getUI()).getUserData();
			sessionUserData.setCurrentRange(logsRange);
			LOGGER.trace("UI-{}: Logs Init Range: {}-{}", ((PersephoneUI)getUI()).getUIId(), logsRange.getStart(), logsRange.getEnd());

			// Get logs
			String logs = logsService.getLogs(app, logsRange);

			String logsPath = getLogsFilePath(env);

			// Create UI for logs
			Panel logsPanel = getLogsPanel(app, logs, logsPath);
			this.addComponent(new PageHeader(app, "Logs", getDownloadButton(app), autoScrollButton));
			this.addComponent(logsPanel);
		}
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		// when leaving the view, remove the setInterval JS function
		ajaxRefreshDestroy();

		// then call default beforeLeave method
		View.super.beforeLeave(event);
	}

	private Label getNoLogsText(Application app, Environment env, String loggingPath, String loggingFile) {
		String loggingPathUnavailable;
		if(StringUtils.isEmpty(loggingPath)) {
			loggingPathUnavailable = "- Property 'logging.path' is not set";
		} else {
			loggingPathUnavailable = String.format("- Property 'logging.path' is set to '%s'", loggingPath);
		}

		String loggingFileUnavailable;
		if(StringUtils.isEmpty(loggingFile)) {
			loggingFileUnavailable = "- Property 'logging.file' is not set";
		} else {
			loggingFileUnavailable = String.format("- Property 'logging.file' is set to '%s'", loggingFile);
		}

		StringBuilder noLogsText = new StringBuilder()
								.append(String.format("Endpoint %s is not available because:", app.endpoints().logfile()))
								.append("\n")
								.append(loggingPathUnavailable)
								.append("\n")
								.append(loggingFileUnavailable)
								.append("\n\n")
								.append("At least one of those properties have to be properly set.")
								.append("\n\n")
								.append("Also, the default log file name is 'spring.log', "
										+ "if yours is not whatever.log you have to set logging.file to 'whatever.log'.")
								;

		if(!StringUtils.isEmpty(env.get("LOG_FILE"))) {
			noLogsText
				.append("\n\n")
				.append(String.format("Generated value for LOG_FILE property: %s", env.get("LOG_FILE")));
		}

		return new Label(noLogsText.toString(), ContentMode.PREFORMATTED);
	}

	private String getLogsFilePath(Environment env) {

		if(!StringUtils.isEmpty(env.get("LOG_FILE"))) {
			return env.get("LOG_FILE");
		}

		if(!StringUtils.isEmpty(env.get("logging.file"))) {
			return env.get("logging.file");
		}

		if(!StringUtils.isEmpty(env.get("logging.path"))) {
			return env.get("logging.path");
		}

		return "";
	}

	private Panel getLogsPanel(Application app, String logs, String propertyLoggingPath) {

		// Logs text area
		Label logsLabel = new Label(logs, ContentMode.PREFORMATTED);
		logsLabel.setStyleName("app-logs");

		// Create panel
		VerticalLayout layout = new VerticalLayout(logsLabel);
		layout.setSpacing(false);
		layout.setSizeUndefined();

		Panel panel = new Panel(String.format("Application Logs (from %s)", propertyLoggingPath), layout);
		panel.setHeight(500, Unit.PIXELS);
		scrollToBottom(panel);

		// Auto refresh logs
		ajaxRefreshInit(args -> {
			int uiId = ((PersephoneUI)getUI()).getUIId();
			LOGGER.trace("UI-{}: Logs Refresh Start", uiId);

			// Get current session range
			LogsRange currentSessionRange = ((PersephoneUI)getUI()).getUserData().getCurrentRange();
			LOGGER.trace("UI-{}: Logs Refresh: Current Range: {}-{}", uiId, currentSessionRange.getStart(), currentSessionRange.getEnd());

			// Get next logs range to retrieve
			LogsRange nextRange = logsService.getLogsRange(app, currentSessionRange, bytesToRetrieveRefresh);
			LOGGER.trace("UI-{}: Logs Refresh: Next Range: {}-{}", uiId, nextRange.getStart(), nextRange.getEnd());

			// Update current range into user session
			((PersephoneUI)getUI()).getUserData().setCurrentRange(nextRange);

			// Get logs
			String newLogs = logsService.getLogs(app, nextRange);

			// Update UI
			if(!StringUtils.isEmpty(newLogs)) {

				String oldLogs = logsLabel.getValue();

				// Too much logs displayed => let's strip them out
				if(oldLogs.length() + newLogs.length() > bytesToDisplayMax) {
					newLogs = oldLogs + newLogs;
					LOGGER.trace("UI-{}: Logs Refresh: Too much logs are going to be displayed (length={}), it will be stripped to {} chars", uiId, newLogs.length(), bytesToDisplayMax);
					newLogs = newLogs.substring(newLogs.length() - bytesToDisplayMax);
					logsLabel.setValue(newLogs);
				}
				// max length not reached yet
				else {
					logsLabel.setValue(oldLogs + newLogs);
				}
			}

			scrollToBottom(panel);

			LOGGER.trace("UI-{} Logs Refresh End", uiId);
		});

		return panel;
	}

	private void scrollToBottom(Panel panel) {
		if(((PersephoneUI)getUI()).getUserData().isTailAutoScrollEnabled()) {
			panel.setScrollTop(Integer.MAX_VALUE);
		}
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
		downloadButton.addClickListener(e -> fileDownloader.setFileDownloadResource(getLogsStream(app)));
		return downloadButton;
	}

	private Button getAutoScrollButton() {
		Button btn = new Button();
		updateAutoScrollButtonCaption(btn);

		btn.addClickListener(e -> {
			((PersephoneUI)getUI()).getUserData().toggleTailAutoScroll();

			updateAutoScrollButtonCaption(btn);
		});

		return btn;
	}

	private void updateAutoScrollButtonCaption(Button btn) {
		if(((PersephoneUI)getUI()).getUserData().isTailAutoScrollEnabled()) {
			btn.setCaption("Disable auto scroll");
		} else {
			btn.setCaption("Enable auto scroll");
		}
	}

	private StreamResource getLogsStream(Application app) {
		return new StreamResource(() -> {
			try (InputStream logsStream = logsService.downloadLogs(app).getInputStream()) {
				return logsStream;
			} catch (IOException ex) {
				throw new ApplicationRuntimeException(app, String.format("Unable to get logs file: %s", ex.getMessage()));
			}
		}, String.format("logs-%s-%s-%s.txt", app.getName(), app.getEnvironment(), (new Date()).getTime()));
	}
}
