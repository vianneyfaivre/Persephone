package re.vianneyfaiv.persephone.ui.page;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.ENDPOINTS)
public class ApplicationEndpointsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogsPage.class);

	@Autowired
	private ApplicationService appService;

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
			// TODO: throw exception
		}

		// Header
		this.addComponent(new PageHeader(app.get(), "Endpoints"));

		// Add endpoints links
		app.get()
		   .endpoints().asList().stream()
		   .forEach(endpointUrl -> this.addComponent(new Link(endpointUrl, new ExternalResource(endpointUrl), "_blank", 0, 0, BorderStyle.DEFAULT)));
	}
}
