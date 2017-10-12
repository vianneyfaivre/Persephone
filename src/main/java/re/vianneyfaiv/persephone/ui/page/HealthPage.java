package re.vianneyfaiv.persephone.ui.page;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Health;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.HealthService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.HealthCard;
import re.vianneyfaiv.persephone.ui.component.PageHeader;

@UIScope
@SpringView(name=PersephoneViews.HEALTH)
public class HealthPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private HealthService healthService;

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

		Health health = healthService.getHealth(app.get());

		HorizontalLayout cards = new HorizontalLayout();

		if(health.getDiskSpace().isPresent()) {
			long diskFree = health.getDiskSpace().get().getFree();
			long diskTotal = health.getDiskSpace().get().getTotal();

			long percentageFree = (diskFree * 100) / diskTotal;

			cards.addComponent(new HealthCard("Disk",
								health.getDiskSpace().get().getStatus(),
								String.format("Free diskspace: %s %%", percentageFree)));
		}

		if(health.getDb().isPresent()) {
			cards.addComponent(new HealthCard("Database",
					health.getDiskSpace().get().getStatus(),
					String.format("Vendor: %s", health.getDb().get().getDatabase())));
		}

		this.addComponent(new PageHeader(app.get(), "Health"));

		if(cards.getComponentCount() == 0) {
			this.addComponent(new Label("No additional data to display"));
		} else {
			this.addComponent(cards);
		}
	}
}
