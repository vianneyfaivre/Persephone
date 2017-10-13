package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Loggers;
import re.vianneyfaiv.persephone.exception.ApplicationRuntimeException;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LoggersService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.LoggerGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.LOGGERS)
public class LoggersPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggersPage.class);

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LoggersService loggersService;

	private Grid<LoggerGridRow> grid;
	private List<LoggerGridRow> loggersRows;
	private TextField filterInput;

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
		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO throw exception
		}

		// Get loggers config
		Optional<Loggers> loggers = getLoggers(app.get());

		if(loggers.isPresent()) {
			
			// Display loggers in a grid
			grid = new Grid<>(LoggerGridRow.class);
			
			grid.removeAllColumns();
			
			Column<LoggerGridRow, String> defaultSortColumn = grid.addColumn(LoggerGridRow::getName).setCaption("Name");
			grid.addComponentColumn(logger -> {
				NativeSelect<String> levelsDropdown = new NativeSelect<>(null, loggers.get().getLevels());
				
				levelsDropdown.setEmptySelectionAllowed(false);
				levelsDropdown.setSelectedItem(logger.getLevel());
				
				// on selected level
				levelsDropdown.addValueChangeListener(value -> {
					
					// change logger level
					loggersService.changeLevel(app.get(), logger.getName(), value.getValue());
					
					// refresh data in grid (several loggers might have been impacted)
					updateLoggers(app.get());
					
					Notification.show(
							String.format("Logger %s level changed to %s", logger.getName(), value.getValue())
							, Notification.Type.TRAY_NOTIFICATION);
				});
				
				return levelsDropdown;
			}).setCaption("Level");
			grid.setRowHeight(40);
			
			grid.setItems(loggersRows);
			grid.sort(defaultSortColumn);
			
			grid.setSizeFull();
			
			// Filter grid by logger name
			filterInput = new TextField();
			filterInput.setPlaceholder("filter by logger name...");
			filterInput.addValueChangeListener(e -> filterLoggers(e.getValue()));
			filterInput.setValueChangeMode(ValueChangeMode.LAZY);

			this.addComponent(new PageHeader(app.get(), "Loggers", filterInput));
			this.addComponent(new Label("Changing a level will update one/many logger(s) level(s)"));
			this.addComponent(grid);
		} else {
			this.addComponent(new PageHeader(app.get(), "Loggers"));
			this.addComponent(new Label(String.format("Failed to call %s<br />This endpoint is available since Spring Boot 1.5", app.get().endpoints().loggers()), ContentMode.HTML));
		}
	}

	private Optional<Loggers> getLoggers(Application app) {
		
		try {
			Loggers loggers = loggersService.getLoggers(app);
	
			// Convert loggers to grid rows
			this.loggersRows = loggers.getLoggers()
									.entrySet().stream()
									.map(LoggerGridRow::new)
									.collect(Collectors.toList());
			return Optional.of(loggers);
		} catch (ApplicationRuntimeException ignored) {
			LOGGER.warn(ignored.getMessage());
		}
		
		return Optional.empty();
	}

	private void filterLoggers(String filterValue) {

		if(StringUtils.isEmpty(filterValue)) {
			this.grid.setItems(this.loggersRows);
		}
		else {
			this.grid.setItems(
				this.loggersRows
					.stream()
					.filter(logger -> logger.getName().toLowerCase().contains(filterValue.toLowerCase()))
			);
		}
	}

	private void updateLoggers(Application app) {
		this.getLoggers(app);
		this.filterLoggers(this.filterInput.getValue());
	}
}

