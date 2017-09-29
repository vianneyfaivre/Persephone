package re.vianneyfaiv.persephone.ui.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.annotation.PostConstruct;

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

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LogsService logsService;

	@PostConstruct
	public void init() {
	}

	@Override
	public void enter(ViewChangeEvent event) {

		// Get application
		int appId = Integer.valueOf(event.getParameters());
		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO: throw exception
		}

		// Title
		String title = String.format("<h2>%s (%s): Logs</h2>", app.get().getName(), app.get().getEnvironment());
		this.addComponent(new Label(title, ContentMode.HTML));

		// Download logs button ByteArrayResource logsBytes = logsService.downloadLogs(app.get());
		Button downloadButton = new Button("Download full logfile");
		this.addComponent(downloadButton);

		try (InputStream logsStream = logsService.downloadLogs(app.get()).getInputStream()) {
			StreamResource resource = new StreamResource(() -> logsStream, "logs.txt");

			FileDownloader fileDownloader = new FileDownloader(resource);
			fileDownloader.setOverrideContentType(false);
			fileDownloader.extend(downloadButton);
		} catch (IOException e) {
			throw new PersephoneServiceException(app.get(), String.format("Unable to get logs file: %s", e.getMessage()));
		}

		// Back button
		this.addComponent(new Button("Back to applications list", e -> getUI().getNavigator().navigateTo(PersephoneViews.APPLICATIONS)));

		// Get logs
		String logs = logsService.getLogs(app.get());
		this.addComponent(new Label(logs, ContentMode.PREFORMATTED));
	}

}