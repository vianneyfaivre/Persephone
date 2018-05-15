package re.vianneyfaiv.persephone.ui.page;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

/**
 * Page that lists Actuator URLs used for an application
 */
@UIScope
@SpringView(name=PersephoneViews.ENDPOINTS)
public class EndpointsPage extends VerticalLayout implements View {

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
		int appId = Integer.parseInt(event.getParameters());

		Application app = pageHelper.getApp(appId);

		// Header
		this.addComponent(new PageHeader(app, "Endpoints"));

		// Add endpoints links
		app.endpoints().asList().stream()
		   .forEach(endpointUrl -> this.addComponent(new Link(endpointUrl, new ExternalResource(endpointUrl), "_blank", 0, 0, BorderStyle.DEFAULT)));
	}
}
