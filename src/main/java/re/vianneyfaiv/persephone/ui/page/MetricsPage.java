package re.vianneyfaiv.persephone.ui.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.metrics.MetricsCache;
import re.vianneyfaiv.persephone.domain.metrics.MetricsDatasource;
import re.vianneyfaiv.persephone.domain.metrics.MetricsRest;
import re.vianneyfaiv.persephone.domain.metrics.MetricsSystem;
import re.vianneyfaiv.persephone.service.MetricsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.Card;
import re.vianneyfaiv.persephone.ui.component.MetricsCacheGridRow;
import re.vianneyfaiv.persephone.ui.component.MetricsGridRow;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.Formatters;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.METRICS)
public class MetricsPage extends VerticalLayout implements View {

	@Autowired
	private MetricsService metricsService;

	@Autowired
	private PageHelper pageHelper;

	private List<MetricsGridRow> allMetricsRows;
	private Grid<MetricsGridRow> allMetricsGrid;

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

		// Get metrics
		Map<String, Number> metrics = metricsService.getAllMetrics(app);
		Collection<MetricsCache> metricsCaches = metricsService.getMetricsCaches(metrics);
		List<MetricsRest> metricsRest = metricsService.getMetricsRest(metrics);
		Collection<MetricsDatasource> metricsDb = metricsService.getMetricsDatasources(metrics);
		MetricsSystem metricsSystem = metricsService.getSystemMetrics(metrics);

		// Build UI
		this.addComponent(new PageHeader(app, "Metrics"));

		this.addComponent(new Label("<h3>System metrics</h3>", ContentMode.HTML));
		this.addComponent(getSystemPanel(metricsSystem));

		if(!metricsRest.isEmpty()) {
			this.addComponent(new Label("<h3>Rest Controllers metrics</h3>", ContentMode.HTML));
			this.addComponent(getRestGrid(metricsRest));
		}

		if(!metricsCaches.isEmpty()) {
			this.addComponent(new Label("<h3>Cache metrics</h3>", ContentMode.HTML));
			this.addComponent(getCacheGrid(metricsCaches));
		}

		if(!metricsDb.isEmpty()) {
			this.addComponent(new Label("<h3>Datasource metrics</h3>", ContentMode.HTML));
			this.addComponent(getDatasourceGrid(metricsDb));
		}

		allMetricsGrid = getAllMetricsGrid(metrics);

		TextField filterInput = new TextField();
		filterInput.setPlaceholder("filter by metric...");
		filterInput.addValueChangeListener(e -> updateMetrics(e.getValue()));
		filterInput.setValueChangeMode(ValueChangeMode.LAZY);

		this.addComponent(new Label("<h3>All metrics</h3>", ContentMode.HTML));
		this.addComponent(filterInput);
		this.addComponent(allMetricsGrid);
	}

	private Component getSystemPanel(MetricsSystem metrics) {

		// System
		String systemLoad = metrics.getSystemLoadAverage() != -1 ? String.format("System Load Average: %s", metrics.getSystemLoadAverage()) : "";
		Card sys = new Card("System",
							String.format("Processors: %s", metrics.getProcessors()),
							systemLoad,
							String.format("Uptime: %s", Formatters.readableDuration(metrics.getUptime()))
						);

		// Heap + Memory
		String memAllocated = Formatters.readableFileSize(metrics.getMemAllocated() * 1000);
		String memTotal = Formatters.readableFileSize(metrics.getMem() * 1000);
		String heapUsed = Formatters.readableFileSize(metrics.getHeapUsed() * 1000);
		String heapMin = Formatters.readableFileSize(metrics.getHeapInit() * 1000);
		String heapMax = Formatters.readableFileSize(metrics.getHeap() * 1000);
		String heapCommitted = Formatters.readableFileSize(metrics.getHeapCommitted() * 1000);
		Card mem = new Card("Memory",
							String.format("Memory: %s / %s (%s%% free)", memAllocated, memTotal, metrics.getMemFreePercentage()),
							String.format("Heap committed: %s / %s (%s%% free)", heapUsed, heapCommitted, metrics.getHeapCommittedFreePercentage()),
							String.format("Heap min: %s", heapMin),
							String.format("Heap max: %s", heapMax)
						);

		// Threads
		List<String> threadsInfos = new ArrayList<>();
		threadsInfos.add(String.format("Active: %s", metrics.getThreads()));

		if(metrics.getThreadPeak() >= 0) {
			threadsInfos.add(String.format("Peak: %s", metrics.getThreadPeak()));
		}

		if(metrics.getThreadDaemon() >= 0) {
			threadsInfos.add(String.format("Daemons: %s", metrics.getThreadDaemon()));
		}

		Card threads = new Card("Threads", threadsInfos);

		// Classes
		Card classes = new Card("Classes",
							String.format("Currently Loaded: %s", metrics.getClasses()),
							String.format("Total Loaded: %s", metrics.getClassesLoaded()),
							String.format("Total Unloaded: %s", metrics.getClassesUnloaded())
						);

		// GCs
		List<String> gcInfos = metrics
							.getGarbageCollectionInfos().stream()
							.map(gcInfo -> String.format("%s: called %s times, spent %s", gcInfo.getName(), gcInfo.getCount(), Formatters.readableDuration(gcInfo.getTime())))
							.collect(Collectors.toList());

		Card gc = new Card("Garbage Collection", gcInfos);

		return new HorizontalLayout(sys, mem, gc, threads, classes);
	}

	private void updateMetrics(String filterValue) {

		if(StringUtils.isEmpty(filterValue)) {
			this.allMetricsGrid.setItems(this.allMetricsRows);
		}
		else {
			this.allMetricsGrid.setItems(
				this.allMetricsRows
					.stream()
					.filter(p -> p.getName().toLowerCase().contains(filterValue.toLowerCase()))
			);
		}
	}

	private Grid<MetricsRest> getRestGrid(Collection<MetricsRest> metrics) {

		List<MetricsRest> metricsItems = metrics.stream()
													.filter(MetricsRest::valid)
													.collect(Collectors.toList());

		Grid<MetricsRest> gridCache = new Grid<>(MetricsRest.class);
		gridCache.removeAllColumns();
		gridCache.addColumn(MetricsRest::getName).setCaption("Path");
		gridCache.addColumn(m -> m.getStatus() + " " + m.getStatus().getReasonPhrase()).setCaption("HTTP Status");
		gridCache.addColumn(MetricsRest::getValue).setCaption("Hits");
		gridCache.addColumn(MetricsRest::getLastResponseTime).setCaption("Last Response Time (ms)");

		gridCache.setItems(metricsItems);
		gridCache.setHeightByRows(metricsItems.size());
		gridCache.setWidth(100, Unit.PERCENTAGE);

		return gridCache;
	}


	private Grid<MetricsDatasource> getDatasourceGrid(Collection<MetricsDatasource> metrics) {

		Grid<MetricsDatasource> grid = new Grid<>(MetricsDatasource.class);
		grid.removeAllColumns();
		grid.addColumn(MetricsDatasource::getName).setCaption("Datasource name");
		grid.addColumn(MetricsDatasource::getActiveConnections).setCaption("Active connections");
		grid.addColumn(MetricsDatasource::getConnectionPoolUsage).setCaption("Connection pool usage");

		grid.setItems(metrics);
		grid.setHeightByRows(metrics.size());

		return grid;
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
		allMetricsRows = metrics.entrySet().stream()
								.map(MetricsGridRow::new)
								.collect(Collectors.toList());

		Grid<MetricsGridRow> grid = new Grid<>(MetricsGridRow.class);
		grid.removeAllColumns();
		Column<MetricsGridRow, String> defaultSortColumn = grid.addColumn(MetricsGridRow::getName).setCaption("Name");
		grid.addColumn(MetricsGridRow::getValue).setCaption("Value");

		grid.setItems(allMetricsRows);
		grid.sort(defaultSortColumn);
		grid.setSizeFull();
		return grid;
	}
}
