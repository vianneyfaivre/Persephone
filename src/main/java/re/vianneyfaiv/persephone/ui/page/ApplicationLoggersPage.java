package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Loggers;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LoggersService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.ButtonBar;
import re.vianneyfaiv.persephone.ui.component.LoggerGridRow;
import re.vianneyfaiv.persephone.ui.component.PageTitle;

@UIScope
@SpringView(name=PersephoneViews.LOGGERS)
public class ApplicationLoggersPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LoggersService loggersService;

	@PostConstruct
	public void init() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.removeAllComponents();

		// Get application
		int appId = Integer.valueOf(event.getParameters());
		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO throw exception
		}

		// Get loggers config
		Loggers loggers = loggersService.getLoggers(app.get());

		// Convert loggers to grid rows
		List<LoggerGridRow> gridRows =
				loggers.getLoggers()
				.entrySet().stream()
				.map(LoggerGridRow::new)
				.collect(Collectors.toList());

		// Display loggers in a grid
		Grid<LoggerGridRow> loggersGrid = new Grid<>(LoggerGridRow.class);
		loggersGrid.setColumns("name", "level");
		loggersGrid.sort("name");
		loggersGrid.setSizeFull();
		loggersGrid.setItems(gridRows);

		this.addComponent(new PageTitle(app.get(), "Loggers"));
		this.addComponent(new ButtonBar());
		this.addComponent(loggersGrid);
	}
}

