package re.vianneyfaiv.persephone.ui.page;

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
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.ENDPOINTS)
public class EndpointsPage extends VerticalLayout implements View {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsPage.class);

	@Autowired
	private PageHelper pageHelper;

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

		// Header
		this.addComponent(new PageHeader(app, "Endpoints"));

		// Add endpoints links
		app.endpoints().asList().stream()
		   .forEach(endpointUrl -> this.addComponent(new Link(endpointUrl+".json", new ExternalResource(endpointUrl+".json"), "_blank", 0, 0, BorderStyle.DEFAULT)));
	}
}
