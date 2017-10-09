package re.vianneyfaiv.persephone.ui.page;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.PropertyItem;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.PROPERTIES)
public class ApplicationPropertiesPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private EnvironmentService envService;

	private Environment currentEnv;
	private Grid<PropertyItem> propertiesGrid;

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

		// Load properties
		this.currentEnv = envService.getEnvironment(app.get());

		// Properties grid
		this.propertiesGrid = new Grid<>(PropertyItem.class);

		this.propertiesGrid.removeAllColumns();
		this.propertiesGrid.addColumn(PropertyItem::getKey).setCaption("Property")
							.getSortOrder(SortDirection.ASCENDING);
		this.propertiesGrid.addColumn(PropertyItem::getValue).setCaption("Value");
		this.propertiesGrid.addColumn(PropertyItem::getOrigin).setCaption("Origin");

		this.propertiesGrid.setSizeFull();

		// Filter by Property
		TextField filterInput = new TextField();
		filterInput.setPlaceholder("filter by property key...");
		filterInput.addValueChangeListener(e -> updateProperties(e.getValue()));
		filterInput.setValueChangeMode(ValueChangeMode.LAZY);

		this.addComponent(new PageHeader(app.get(), "Properties", filterInput));
		this.addComponent(this.propertiesGrid);
		this.updateProperties("");
	}

	private void updateProperties(String filterByPropKey) {

		if(StringUtils.isEmpty(filterByPropKey)) {
			this.propertiesGrid.setItems(this.currentEnv.getProperties());
		}
		else {
			this.propertiesGrid.setItems(
				this.currentEnv.getProperties()
					.stream()
					.filter(p -> p.getKey().toLowerCase().contains(filterByPropKey.toLowerCase()))
			);
		}
	}

}
