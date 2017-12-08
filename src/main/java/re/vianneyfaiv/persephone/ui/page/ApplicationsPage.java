package re.vianneyfaiv.persephone.ui.page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.ItemClickListener;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.env.ActuatorVersion;
import re.vianneyfaiv.persephone.domain.env.Environment;
import re.vianneyfaiv.persephone.domain.metrics.Metrics;
import re.vianneyfaiv.persephone.exception.ApplicationRuntimeException;
import re.vianneyfaiv.persephone.service.v1.ApplicationService;
import re.vianneyfaiv.persephone.service.v1.EnvironmentService;
import re.vianneyfaiv.persephone.service.v1.MetricsService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.fragment.ApplicationOverviewPanel;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

/**
 * Page that lists applications.
 *
 * When selecting an application, a details panel will be displayed.
 *
 * {@link ApplicationOverviewPanel}
 */
@UIScope
@SpringView(name=PersephoneViews.APPLICATIONS)
public class ApplicationsPage extends HorizontalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private EnvironmentService envService;

	@Autowired
	private MetricsService metricsService;

	@Autowired
	private PageHelper pageHelper;

	private ApplicationOverviewPanel details;
	private List<Application> applications;
	private Grid<Application> grid;

	@PostConstruct
	public void init() {

		applications = this.appService.findAll();

		// Title
		Label title = new Label("<h2>Applications</h2>", ContentMode.HTML);

		initApplicationsGrid();

		// Build layout
		VerticalLayout leftLayout = new VerticalLayout(title, grid);
		leftLayout.setMargin(false);
		this.addComponent(leftLayout);

		// Center align layout
		this.setWidth("100%");
		this.setMargin(new MarginInfo(false, true));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pageHelper.setErrorHandler(this);
	}

	private void initApplicationsGrid() {

		grid = new Grid<>();

		// Columns
		grid.removeAllColumns();
		Column<Application, String> appColumn = grid.addColumn(Application::getName).setCaption("Application").setExpandRatio(0);
		Column<Application, String> envColumn = grid.addColumn(Application::getEnvironment).setCaption("Environment").setExpandRatio(0);
		Column<Application, String> urlColumn = grid.addColumn(Application::getUrl).setCaption("URL").setExpandRatio(1);

		// Items
		grid.setItems(applications);

		// Header filter row
		initFilterRow(appColumn, envColumn, urlColumn);

		// Style
		grid.setStyleGenerator(app -> app.isUp() ? null : "app-down");
		grid.addItemClickListener(applicationOnClick());
		grid.setSizeFull();
		grid.setHeightByRows(applications.size());
		grid.sort(appColumn);
		grid.setRowHeight(40);
	}

	private void initFilterRow(Column<Application, String> appColumn, Column<Application, String> envColumn, Column<Application, String> urlColumn) {
		TextField filterApp = new TextField();
		TextField filterEnv = new TextField();
		TextField filterUrl = new TextField();

		filterApp.setPlaceholder("filter by application...");
		filterApp.addValueChangeListener(e -> updateApplications(e.getValue(), filterEnv.getValue(), filterUrl.getValue()));
		filterApp.setValueChangeMode(ValueChangeMode.LAZY);
		filterApp.focus();
		filterApp.setSizeFull();

		filterEnv.setPlaceholder("filter by environment...");
		filterEnv.addValueChangeListener(e -> updateApplications(filterApp.getValue(), e.getValue(), filterUrl.getValue()));
		filterEnv.setValueChangeMode(ValueChangeMode.LAZY);
		filterEnv.setSizeFull();

		filterUrl.setPlaceholder("filter by URL...");
		filterUrl.addValueChangeListener(e -> updateApplications(filterApp.getValue(), filterEnv.getValue(), e.getValue()));
		filterUrl.setValueChangeMode(ValueChangeMode.LAZY);
		filterUrl.setSizeFull();

		// Header row
		HeaderRow filterRow = grid.addHeaderRowAt(grid.getHeaderRowCount());
		filterRow.getCell(appColumn).setComponent(filterApp);
		filterRow.getCell(envColumn).setComponent(filterEnv);
		filterRow.getCell(urlColumn).setComponent(filterUrl);
	}

	private ItemClickListener<Application> applicationOnClick() {
		return e -> {

			// Remove previous panel
			if(details!=null) {
				this.removeComponent(this.details);
			}

			// Get application overview info
			Application app = e.getItem();
			ActuatorVersion actuatorVersion = this.envService.getActuatorVersion(app);

			if(actuatorVersion == ActuatorVersion.NOT_SUPPORTED) {
				throw new ApplicationRuntimeException(app, "Actuator version is not supported");
			}

			Environment env = this.envService.getEnvironment(app);
			Metrics metrics = this.metricsService.getMetrics(app);

			// No exception has been thrown : app is up and running !
			appService.setUp(app, true);

			// Add overview panel to page
			this.details = new ApplicationOverviewPanel(app, env, metrics);
			this.addComponent(this.details);
		};
	}

	private void updateApplications(String filterApp, String filterEnv, String filterUrl) {

		List<Application> filteredApps = new ArrayList<>(this.applications);

		if(!StringUtils.isEmpty(filterApp)) {
			filteredApps = filteredApps.stream()
				.filter(app -> app.getName().toLowerCase().contains(filterApp.toLowerCase()))
				.collect(Collectors.toList());
		}

		if(!StringUtils.isEmpty(filterEnv)) {
			filteredApps = filteredApps.stream()
				.filter(app -> app.getEnvironment().toLowerCase().contains(filterEnv.toLowerCase()))
				.collect(Collectors.toList());
		}

		if(!StringUtils.isEmpty(filterUrl)) {
			filteredApps = filteredApps.stream()
				.filter(app -> app.getUrl().toLowerCase().contains(filterUrl.toLowerCase()))
				.collect(Collectors.toList());
		}

		this.grid.setItems(filteredApps);
	}
}
