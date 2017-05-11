package re.vianneyfaiv.persephone.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Title;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.service.ApplicationService;
import re.vianneyfaiv.persephone.service.EnvironmentService;
import re.vianneyfaiv.persephone.service.PersephoneServiceException;
import re.vianneyfaiv.persephone.ui.page.ApplicationsPage;

@Title("Persephone")
@SpringUI
public class PersephoneUI extends UI {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private EnvironmentService envService;

	@Override
	protected void init(VaadinRequest request) {

		List<Application> apps = this.appService.findAll();

		ApplicationsPage appsPage = new ApplicationsPage(apps);
		appsPage.setApplicationClickListener(e -> {
			appsPage.updateView(this.envService.getEnvironment(e.getItem()));
		});

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(appsPage);
		this.setContent(layout);

		UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
			@Override
		    public void error(com.vaadin.server.ErrorEvent event) {
				for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {
					if(t instanceof PersephoneServiceException) {

						appsPage.resetDetailsFragment();

						PersephoneServiceException e = (PersephoneServiceException) t;

						new Notification(
								"Unable to reach " + e.getApplication().getUrl(),
							    e.getMessage(),
							    Notification.Type.ERROR_MESSAGE,
							    true)
							.show(Page.getCurrent());
					}
				}
			}
		});
	}
}
