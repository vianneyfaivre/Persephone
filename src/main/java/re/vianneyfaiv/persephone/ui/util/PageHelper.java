package re.vianneyfaiv.persephone.ui.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Layout;

import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.exception.ApplicationNotFoundException;
import re.vianneyfaiv.persephone.service.ApplicationService;

@Component
public class PageHelper {

	@Autowired
	private ApplicationService appService;

	/**
	 * Set component error handler with the one from UI.
	 * This is required because when an exception is thrown when calling Navigator#navigateTo it won't be handled by UI' error handler
	 */
	public void setErrorHandler(Layout page) {
		page.setErrorHandler(page.getUI().getErrorHandler());
	}

	public void setLayoutStyle(AbstractOrderedLayout page) {
		// Center align layout
		page.setWidth("100%");
		page.setMargin(new MarginInfo(false, true));
	}

	public Application getApp(int appId) {
		Optional<Application> app = appService.findById(appId);
		if(!app.isPresent()) {
			throw new ApplicationNotFoundException(appId);
		}
		return app.get();
	}
}
