package re.vianneyfaiv.persephone.ui.page;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.env.Environment;
import re.vianneyfaiv.persephone.domain.env.PropertyItem;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

/**
 * Display all declared properties of an application
 */
@UIScope
@SpringView(name=PersephoneViews.PROPERTIES)
public class PropertiesPage extends VerticalLayout implements View {

	@Autowired
	private EnvironmentService envService;

	@Autowired
	private PageHelper pageHelper;

	private Environment currentEnv;
	private Grid<PropertyItem> grid;

	@PostConstruct
	public void init() {
		pageHelper.setLayoutStyle(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pageHelper.setErrorHandler(this);

		this.removeAllComponents();

		// Get application
		int appId = Integer.parseInt(event.getParameters());
		Application app = pageHelper.getApp(appId);

		// Load properties
		this.currentEnv = envService.getEnvironment(app);

		// Properties grid
		this.grid = new Grid<>(PropertyItem.class);

		this.grid.removeAllColumns();
		Column<PropertyItem, String> propertyColumn = this.grid.addColumn(PropertyItem::getKey)
																.setCaption("Property")
																.setExpandRatio(1);
		this.grid.addColumn(PropertyItem::getValue)
					.setCaption("Value")
					.setExpandRatio(1);
		this.grid.addColumn(PropertyItem::getOrigin).setCaption("Origin");

		this.grid.sort(propertyColumn);
		this.grid.setSizeFull();
		this.grid.setRowHeight(40);

		// Filter by Property
		TextField filterInput = new TextField();
		filterInput.setPlaceholder("filter by property key...");
		filterInput.addValueChangeListener(e -> updateProperties(e.getValue()));
		filterInput.setValueChangeMode(ValueChangeMode.LAZY);
		filterInput.setSizeFull();

		// Header row
		HeaderRow filterRow = grid.addHeaderRowAt(grid.getHeaderRowCount());
		filterRow.getCell(propertyColumn).setComponent(filterInput);

		this.addComponent(new PageHeader(app, "Properties"));
		this.addComponent(this.grid);
		this.updateProperties("");
	}

	private void updateProperties(String filterByPropKey) {

		if(StringUtils.isEmpty(filterByPropKey)) {
			this.grid.setItems(this.currentEnv.getProperties());
		}
		else {
			this.grid.setItems(
				this.currentEnv.getProperties()
					.stream()
					.filter(p -> p.getKey().toLowerCase().contains(filterByPropKey.toLowerCase()))
			);
		}
	}

}
