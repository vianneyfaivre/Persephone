package re.vianneyfaiv.persephone.ui.page;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Loggers;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.LoggersService;
import re.vianneyfaiv.persephone.ui.PersephoneViews;
import re.vianneyfaiv.persephone.ui.component.ButtonBar;
import re.vianneyfaiv.persephone.ui.component.PageTitle;

@UIScope
@SpringView(name=PersephoneViews.LOGGERS)
public class ApplicationLoggersPage extends VerticalLayout implements View {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private LoggersService loggersService;

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

		this.addComponent(new PageTitle(app.get(), "Loggers"));
		this.addComponent(new ButtonBar());

		Loggers loggers = loggersService.getLoggers(app.get());

		loggers.getLoggers().entrySet().stream()
				.forEach(l -> this.addComponent(new Label(l.toString())));
	}

}
