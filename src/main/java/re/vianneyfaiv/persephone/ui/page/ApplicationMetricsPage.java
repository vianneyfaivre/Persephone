package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Map;
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
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.MetricsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.MetricsGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.METRICS)
public class ApplicationMetricsPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private MetricsService metricsService;

	@PostConstruct
	public void init() {
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

		Map<String, Number> metrics = metricsService.getAllMetrics(app.get());

		List<MetricsGridRow> metricsItems = metrics
												.entrySet().stream()
												.map(MetricsGridRow::new)
												.collect(Collectors.toList());

		Grid<MetricsGridRow> grid = new Grid<>(MetricsGridRow.class);
		grid.setItems(metricsItems);
		grid.setSizeFull();

		this.addComponent(new PageHeader(app.get(), "Metrics"));
		this.addComponent(grid);
	}
}
