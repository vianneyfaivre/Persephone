package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.trace.Trace;
import re.vianneyfaiv.persephone.domain.trace.TraceInfo;
import re.vianneyfaiv.persephone.service.TraceService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.TRACE)
public class TracePage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(TracePage.class);

	@Autowired
	private PageHelper pageHelper;

	@Autowired
	private TraceService traceService;

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

		List<Trace> traces = traceService.getTraces(app);

		this.addComponent(new PageHeader(app, "Last HTTP requests"));
		this.addComponent(getTracesGrid(traces));
	}

	private Grid<TraceInfo> getTracesGrid(List<Trace> traces) {

		List<TraceInfo> tracesInfos = traces.stream()
											.map(Trace::getInfo)
											.collect(Collectors.toList());

		Grid<TraceInfo> grid = new Grid<>(TraceInfo.class);
		grid.removeAllColumns();
		grid.addColumn(TraceInfo::getMethod).setCaption("HTTP Method");
		grid.addColumn(TraceInfo::getPath).setCaption("Path");
		grid.addColumn(TraceInfo::getTimeTaken).setCaption("Time taken (ms)");

		grid.setItems(tracesInfos);
		grid.setSizeFull();

		return grid;
	}
}
