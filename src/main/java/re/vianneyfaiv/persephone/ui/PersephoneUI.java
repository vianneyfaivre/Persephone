package re.vianneyfaiv.persephone.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.UI;

import re.vianneyfaiv.persephone.exception.UIErrorHandler;
import re.vianneyfaiv.persephone.ui.page.ApplicationsPage;

/**
 * Vaadin UI initializer
 */
@Title("Persephone")
@Theme("persephone")
@SpringUI
@SpringViewDisplay
public class PersephoneUI extends UI {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersephoneUI.class);

	@Autowired
	private ApplicationsPage appsPage;

	@Override
	protected void init(VaadinRequest request) {

		// Build UI
		this.setContent(this.appsPage);

		System.out.println("PersephoneUI "+getUI().getUIId());
		
		// Error handler
		UI.getCurrent().setErrorHandler(new UIErrorHandler());
	}
}
