package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Loggers;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LoggersService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.LoggerGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.LOGGERS)
public class ApplicationLoggersPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LoggersService loggersService;

	private Grid<LoggerGridRow> loggersGrid;
	private List<LoggerGridRow> loggersRows;

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
		this.loggersRows = loggers.getLoggers()
								.entrySet().stream()
								.map(LoggerGridRow::new)
								.collect(Collectors.toList());

		// Display loggers in a grid
		loggersGrid = new Grid<>(LoggerGridRow.class);
		loggersGrid.setColumns("name", "level");
		loggersGrid.sort("name");
		loggersGrid.setSizeFull();
		loggersGrid.setItems(loggersRows);

		// Filter grid by logger name
		TextField filterInput = new TextField();
		filterInput.setPlaceholder("filter by logger name...");
		filterInput.addValueChangeListener(e -> updateLoggers(e.getValue()));
		filterInput.setValueChangeMode(ValueChangeMode.LAZY);

		this.addComponent(new PageHeader(app.get(), "Loggers", filterInput));
		this.addComponent(loggersGrid);
	}

	private void updateLoggers(String filterByLoggerName) {

		if(StringUtils.isEmpty(filterByLoggerName)) {
			this.loggersGrid.setItems(this.loggersRows);
		}
		else {
			this.loggersGrid.setItems(
				this.loggersRows
					.stream()
					.filter(logger -> logger.getName().toLowerCase().contains(filterByLoggerName.toLowerCase()))
			);
		}
	}
}

