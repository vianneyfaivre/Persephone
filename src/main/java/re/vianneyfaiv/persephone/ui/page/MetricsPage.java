package re.vianneyfaiv.persephone.ui.page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.metrics.MetricsCache;
import re.vianneyfaiv.persephone.domain.metrics.MetricsRest;
import re.vianneyfaiv.persephone.service.MetricsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.MetricsCacheGridRow;
import re.vianneyfaiv.persephone.ui.component.MetricsGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.METRICS)
public class MetricsPage extends VerticalLayout implements View {

	@Autowired
	private MetricsService metricsService;

	@Autowired
	private PageHelper pageHelper;

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
		Application app = pageHelper.getApp(appId);

		Map<String, Number> metrics = metricsService.getAllMetrics(app);
		Collection<MetricsCache> metricsCaches = metricsService.getMetricsCaches(metrics);
		List<MetricsRest> metricsRest = metricsService.getMetricsRest(metrics);

		this.addComponent(new PageHeader(app, "Metrics"));

		if(!metricsCaches.isEmpty()) {
			this.addComponent(new Label("<h3>Cache metrics</h3>", ContentMode.HTML));
			this.addComponent(getCacheGrid(metricsCaches));
		}

		if(!metricsRest.isEmpty()) {
			this.addComponent(new Label("<h3>Rest Controllers metrics</h3>", ContentMode.HTML));
			this.addComponent(getRestGrid(metricsRest));
		}

		Grid<MetricsGridRow> grid = getAllMetricsGrid(metrics);
		this.addComponent(new Label("<h3>All metrics</h3>", ContentMode.HTML));
		this.addComponent(grid);
	}

	private Grid<MetricsRest> getRestGrid(Collection<MetricsRest> metrics) {

		List<MetricsRest> metricsItems = metrics.stream()
													.filter(m -> m.valid())
													.collect(Collectors.toList());

		Grid<MetricsRest> gridCache = new Grid<>(MetricsRest.class);
		gridCache.removeAllColumns();
		gridCache.addColumn(MetricsRest::getName).setCaption("Path");
		gridCache.addColumn(m -> m.getStatus() + " " + m.getStatus().getReasonPhrase()).setCaption("HTTP Status");
		gridCache.addColumn(MetricsRest::getValue).setCaption("Hits");
		gridCache.addColumn(MetricsRest::getLastResponseTime).setCaption("Last Response Time (ms)");

		gridCache.setItems(metricsItems);
		gridCache.setHeightByRows(metricsItems.size());
		gridCache.setSizeFull();

		return gridCache;
	}

	private Grid<MetricsCacheGridRow> getCacheGrid(Collection<MetricsCache> metricsCaches) {
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
		return gridCache;
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
