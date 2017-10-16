package re.vianneyfaiv.persephone.ui.page;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.health.Health;
import re.vianneyfaiv.persephone.service.HealthService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.Card;
import re.vianneyfaiv.persephone.ui.component.PageHeader;
import re.vianneyfaiv.persephone.ui.util.PageHelper;

@UIScope
@SpringView(name=PersephoneViews.HEALTH)
public class HealthPage extends VerticalLayout implements View {

	@Autowired
	private HealthService healthService;

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

		Health health = healthService.getHealth(app);

		HorizontalLayout cards = new HorizontalLayout();

		if(health.getDiskSpace().isPresent()) {
			long diskFree = health.getDiskSpace().get().getFree();
			long diskTotal = health.getDiskSpace().get().getTotal();

			long percentageFree = (diskFree * 100) / diskTotal;

			cards.addComponent(new Card("Disk",
								health.getDiskSpace().get().getStatus(),
								String.format("Free diskspace: %s %%", percentageFree)));
		}

		if(health.getDb().isPresent()) {
			cards.addComponent(new Card("Database",
					health.getDiskSpace().get().getStatus(),
					String.format("Vendor: %s", health.getDb().get().getDatabase())));
		}

		this.addComponent(new PageHeader(app, "Health"));
		this.addComponent(new Label(String.format("Application is %s", health.getStatus())));
		if(cards.getComponentCount() == 0) {
			this.addComponent(new Label("No additional data to display"));
		} else {
			this.addComponent(cards);
		}
	}
}
