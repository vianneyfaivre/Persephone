package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.trace.Trace;
import re.vianneyfaiv.persephone.service.TraceService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.component.TraceGridRow;
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
		this.addComponent(new Label(String.format("<h3>Last %s HTTP requests</h3>", traces.size()), ContentMode.HTML));
		this.addComponent(getTracesGrid(traces));
	}

	private Grid<TraceGridRow> getTracesGrid(List<Trace> traces) {

		List<TraceGridRow> tracesInfos = traces.stream()
											.map(TraceGridRow::new)
											.collect(Collectors.toList());


		Grid<TraceGridRow> grid = new Grid<>(TraceGridRow.class);
		grid.removeAllColumns();

		grid.addColumn(TraceGridRow::getTimestamp).setCaption("Date");
		grid.addColumn(t -> t.getMethod() + " " + t.getPath()).setCaption("HTTP Request");
		grid.addColumn(t -> t.getResponseHttp() + " " + t.getResponseHttp().getReasonPhrase()).setCaption("HTTP Response");
		grid.addColumn(t -> t.getTimeTaken().isPresent() ? t.getTimeTaken().get().toMillis() : -1).setCaption("Time taken (ms)");

		this.requestDetailsPopup(grid);

		grid.setItems(tracesInfos);
		grid.setSizeFull();

		return grid;
	}

	private void requestDetailsPopup(Grid<TraceGridRow> grid) {
		VerticalLayout popupContent = new VerticalLayout();
		PopupView popup = new PopupView(null, popupContent);

		grid.addItemClickListener(e -> {
			popup.setData(e.getItem());
			popup.setPopupVisible(true);
		});

		popup.addPopupVisibilityListener(event -> {
			if (event.isPopupVisible()) {
				popupContent.removeAllComponents();

				TraceGridRow item = (TraceGridRow) popup.getData();

				HorizontalLayout title = new HorizontalLayout(
											new Label(String.format("<h3>%s %s</h3>", item.getMethod(), item.getPath()), ContentMode.HTML),
											new Button("Close", e -> popup.setPopupVisible(false))
										);

				VerticalLayout headersReq = formatHeaders("<h4>Request Headers</h4>", item.getRequestHeaders());
				VerticalLayout headersResp = formatHeaders("<h4>Response Headers</h4>", item.getResponseHeaders());

				popupContent.addComponents(title, headersReq, headersResp);
			}
		});

		popup.setHideOnMouseOut(false);

		this.addComponent(popup);
	}

	private VerticalLayout formatHeaders(String title, Map<String, String> headers) {
		String headersStr = headers.entrySet().stream()
										.map(h -> h.getKey()+"="+h.getValue()+"<br />")
										.reduce("", String::concat);

		VerticalLayout headersLayout = new VerticalLayout(
											new Label(title, ContentMode.HTML),
											new Label(headersStr, ContentMode.HTML)
										);

		headersLayout.setMargin(false);

		return headersLayout;
	}
}
