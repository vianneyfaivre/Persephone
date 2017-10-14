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

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.PropertyItem;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

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
		int appId = Integer.valueOf(event.getParameters());
		Application app = pageHelper.getApp(appId);

		// Load properties
		this.currentEnv = envService.getEnvironment(app);

		// Properties grid
		this.grid = new Grid<>(PropertyItem.class);

		this.grid.removeAllColumns();
		Column<PropertyItem, String> defaultSortColumn = this.grid.addColumn(PropertyItem::getKey).setCaption("Property");
		this.grid.addColumn(PropertyItem::getValue).setCaption("Value");
		this.grid.addColumn(PropertyItem::getOrigin).setCaption("Origin");

		this.grid.sort(defaultSortColumn);
		this.grid.setSizeFull();

		// Filter by Property
		TextField filterInput = new TextField();
		filterInput.setPlaceholder("filter by property key...");
		filterInput.addValueChangeListener(e -> updateProperties(e.getValue()));
		filterInput.setValueChangeMode(ValueChangeMode.LAZY);

		this.addComponent(new PageHeader(app, "Properties", filterInput));
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
