package re.vianneyfaiv.persephone.ui.page;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.PropertyItem;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;

@UIScope
@SpringView(name=PersephoneViews.PROPERTIES)
public class ApplicationPropertiesPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private EnvironmentService envService;

	@PostConstruct
	public void init() {
	}

	@Override
	public void enter(ViewChangeEvent event) {

		this.removeAllComponents();

		// Get application
		int appId = Integer.valueOf(event.getParameters());
		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			// TODO throw exception
		}

		// Title
		String title = String.format("<h2>%s (%s): Properties</h2>", app.get().getName(), app.get().getEnvironment());
		this.addComponent(new Label(title, ContentMode.HTML));

		// Properties grid
		Environment env = envService.getEnvironment(app.get());
		Grid<PropertyItem> grid = new Grid<>(PropertyItem.class);
		grid.setColumns("key", "value", "origin");
		grid.setItems(env.getProperties());
		grid.sort("key");
		this.addComponent(grid);

		// Back button
		this.addComponent(new Button("Back to applications list", e -> getUI().getNavigator().navigateTo(PersephoneViews.APPLICATIONS)));
	}

}
