package re.vianneyfaiv.persephone.ui.page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
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
import re.vianneyfaiv.persephone.ui.component.grid.TraceGridRow;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

/**
 * Display the last HTTP requests sent to an application
 */
@UIScope
@SpringView(name=PersephoneViews.TRACE)
public class TracePage extends VerticalLayout implements View {

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

		this.addComponent(new PageHeader(app, String.format("Last %s HTTP requests", traces.size())));
		this.addComponent(getTracesGrid(traces));
		this.setSpacing(false);
	}

	private Grid<TraceGridRow> getTracesGrid(List<Trace> traces) {

		List<TraceGridRow> tracesInfos = traces.stream()
											.map(TraceGridRow::new)
											.collect(Collectors.toList());


		Grid<TraceGridRow> grid = new Grid<>(TraceGridRow.class);
		grid.removeAllColumns();

		grid.addColumn(TraceGridRow::getTimestamp).setCaption("Date");
		grid.addColumn(t -> t.getMethod() + " " + t.getPath()).setCaption("HTTP Request").setExpandRatio(1);
		grid.addColumn(t -> t.getResponseHttp() + " " + t.getResponseHttp().getReasonPhrase()).setCaption("HTTP Response");
		grid.addColumn(t -> t.getTimeTaken().isPresent() ? t.getTimeTaken().get().toMillis() : -1).setCaption("Time taken (ms)");

		this.requestDetailsPopup(grid);

		grid.setItems(tracesInfos);
		grid.setSizeFull();
		return grid;
	}

	private void requestDetailsPopup(Grid<TraceGridRow> grid) {
		VerticalLayout popupContent = new VerticalLayout();
		popupContent.setSizeFull();

		PopupView popup = new PopupView(null, popupContent);

		grid.addItemClickListener(e -> {
			popup.setData(e.getItem());
			popup.setPopupVisible(true);
		});

		popup.addPopupVisibilityListener(event -> {
			if (event.isPopupVisible()) {
				popupContent.removeAllComponents();

				TraceGridRow item = (TraceGridRow) popup.getData();

				Label popupTitle = new Label(String.format("<h3>%s %s</h3>", item.getMethod(), item.getPath()), ContentMode.HTML);
				popupTitle.setSizeFull();

				Button popupClose = new Button(VaadinIcons.CLOSE);
				popupClose.addClickListener(e -> popup.setPopupVisible(false));

				HorizontalLayout title = new HorizontalLayout(popupTitle, popupClose);
				title.setSizeFull();
				title.setExpandRatio(popupTitle, 3);
				title.setExpandRatio(popupClose, 1);
				title.setComponentAlignment(popupClose, Alignment.TOP_RIGHT);

				VerticalLayout headersReq = formatHeaders("<h4>Request Headers</h4>", item.getRequestHeaders());
				VerticalLayout headersResp = formatHeaders("<h4>Response Headers</h4>", item.getResponseHeaders());

				popupContent.addComponents(title, new HorizontalLayout(headersReq, headersResp));
			}
		});

		popup.setHideOnMouseOut(false);
		popup.setSizeFull();

		this.addComponent(popup);
	}

	private VerticalLayout formatHeaders(String title, Map<String, String> headers) {

		Label titleLabel = new Label(title, ContentMode.HTML);
		titleLabel.setSizeFull();

		Label headersLabel = new Label(headers.entrySet().stream()
										.map(h -> h.getKey()+"="+h.getValue()+"<br />")
										.reduce("", String::concat), ContentMode.HTML);
		headersLabel.setSizeFull();

		VerticalLayout headersLayout = new VerticalLayout(titleLabel, headersLabel);

		headersLayout.setMargin(false);

		return headersLayout;
	}
}
