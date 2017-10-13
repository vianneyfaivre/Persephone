package re.vianneyfaiv.persephone.ui.page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.MetricsCache;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.MetricsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.MetricsCacheGridRow;
import re.vianneyfaiv.persephone.ui.component.MetricsGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.METRICS)
public class MetricsPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private MetricsService metricsService;

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


		Map<String, Number> metrics = metricsService.getAllMetrics(app.get());
		Collection<MetricsCache> metricsCaches = metricsService.getMetricsCaches(metrics);

		this.addComponent(new PageHeader(app.get(), "Metrics"));

		if(!metricsCaches.isEmpty()) {
			List<MetricsCacheGridRow> metricsCacheItems = metricsCaches.stream()
															.map(MetricsCacheGridRow::new)
															.collect(Collectors.toList());

			Grid<MetricsCacheGridRow> gridCache = new Grid<>(MetricsCacheGridRow.class);
			gridCache.removeAllColumns();
			gridCache.addColumn(MetricsCacheGridRow::getName).setCaption("Name");
			gridCache.addColumn(MetricsCacheGridRow::getSize).setCaption("Size");
			gridCache.addColumn(MetricsCacheGridRow::getHit).setCaption("Hit");
			gridCache.addColumn(MetricsCacheGridRow::getMiss).setCaption("Miss");

			gridCache.setItems(metricsCacheItems);
			gridCache.setHeightByRows(metricsCacheItems.size());

			this.addComponent(new Label("<h3>Cache metrics</h3>", ContentMode.HTML));
			this.addComponent(gridCache);
		}

		Grid<MetricsGridRow> grid = getAllMetricsGrid(metrics);
		this.addComponent(new Label("<h3>All metrics</h3>", ContentMode.HTML));
		this.addComponent(grid);
	}

	private Grid<MetricsGridRow> getAllMetricsGrid(Map<String, Number> metrics) {
		List<MetricsGridRow> metricsItems = metrics
												.entrySet().stream()
												.map(MetricsGridRow::new)
												.collect(Collectors.toList());

		Grid<MetricsGridRow> grid = new Grid<>(MetricsGridRow.class);
		grid.removeAllColumns();
		Column<MetricsGridRow, String> defaultSortColumn = grid.addColumn(MetricsGridRow::getName).setCaption("Name");
		grid.addColumn(MetricsGridRow::getValue).setCaption("Value");

		grid.setItems(metricsItems);
		grid.sort(defaultSortColumn);
		grid.setSizeFull();
		return grid;
	}
}
