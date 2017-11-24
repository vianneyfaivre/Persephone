package re.vianneyfaiv.persephone.ui.page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupView;
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
		initGrid();

		this.addComponent(new PageHeader(app, "Properties"));
		this.addComponent(this.popupRowOnClick());
		this.addComponent(this.grid);
		this.updateProperties("", "", "");
	}

	private void initGrid() {
		this.grid = new Grid<>(PropertyItem.class);

		this.grid.removeAllColumns();
		Column<PropertyItem, String> propertyColumn = this.grid.addColumn(PropertyItem::getKey)
																.setCaption("Property")
																.setExpandRatio(1);
		Column<PropertyItem, String> valueColumn = this.grid.addColumn(PropertyItem::getValue)
																.setCaption("Value")
																.setExpandRatio(1);
		Column<PropertyItem, String> originColumn = this.grid.addColumn(PropertyItem::getOrigin).setCaption("Origin");

		this.grid.sort(propertyColumn);
		this.grid.setSizeFull();
		this.grid.setRowHeight(40);

		// distinct origin
		List<String> origins = this.currentEnv.getProperties().stream()
													.map(PropertyItem::getOrigin)
													.distinct()
													.sorted(String::compareTo)
													.collect(Collectors.toList());

		// Filters
		TextField filterProperty = new TextField();
		TextField filterValue = new TextField();
		NativeSelect<String> filterOrigin = new NativeSelect<>(null, origins);


		filterProperty.setPlaceholder("filter by key...");
		filterProperty.addValueChangeListener(e -> updateProperties(e.getValue(), filterValue.getValue(), filterOrigin.getValue()));
		filterProperty.setValueChangeMode(ValueChangeMode.LAZY);
		filterProperty.setSizeFull();

		filterValue.setPlaceholder("filter by value...");
		filterValue.addValueChangeListener(e -> updateProperties(filterProperty.getValue(), e.getValue(), filterOrigin.getValue()));
		filterValue.setValueChangeMode(ValueChangeMode.LAZY);
		filterValue.setSizeFull();

		filterOrigin.addValueChangeListener(e -> updateProperties(filterProperty.getValue(), filterValue.getValue(), e.getValue()));
		filterOrigin.setSizeFull();

		// Header row
		HeaderRow filterRow = grid.addHeaderRowAt(grid.getHeaderRowCount());
		filterRow.getCell(propertyColumn).setComponent(filterProperty);
		filterRow.getCell(valueColumn).setComponent(filterValue);
		filterRow.getCell(originColumn).setComponent(filterOrigin);
	}

	private PopupView popupRowOnClick() {
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

				PropertyItem item = (PropertyItem) popup.getData();

				Label popupTitle = new Label(String.format("<h3>Property '%s' from %s</h3>", item.getKey(), item.getOrigin()), ContentMode.HTML);
				popupTitle.setSizeFull();

				Button popupClose = new Button(VaadinIcons.CLOSE);
				popupClose.addClickListener(e -> popup.setPopupVisible(false));

				HorizontalLayout title = new HorizontalLayout(popupTitle, popupClose);
				title.setSizeFull();
				title.setExpandRatio(popupTitle, 3);
				title.setExpandRatio(popupClose, 1);
				title.setComponentAlignment(popupClose, Alignment.TOP_RIGHT);

				Label propertyValue = new Label(item.getValue(), ContentMode.TEXT);
				propertyValue.setSizeFull();

				popupContent.addComponents(title, propertyValue);
			}
		});

		popup.setHideOnMouseOut(false);
		popup.setSizeFull();

		return popup;
	}

	private void updateProperties(String filterKey, String filterValue, String filterOrigin) {

		List<PropertyItem> filteredProperties = new ArrayList<>(this.currentEnv.getProperties());

		if(!StringUtils.isEmpty(filterKey)) {
			filteredProperties = filteredProperties.stream()
				.filter(prop -> prop.getKey().toLowerCase().contains(filterKey.toLowerCase()))
				.collect(Collectors.toList());
		}

		if(!StringUtils.isEmpty(filterValue)) {
			filteredProperties = filteredProperties.stream()
				.filter(prop -> prop.getValue().toLowerCase().contains(filterValue.toLowerCase()))
				.collect(Collectors.toList());
		}

		if(!StringUtils.isEmpty(filterOrigin)) {
			filteredProperties = filteredProperties.stream()
				.filter(prop -> prop.getOrigin().toLowerCase().contains(filterOrigin.toLowerCase()))
				.collect(Collectors.toList());
		}

		this.grid.setItems(filteredProperties);
	}

}
