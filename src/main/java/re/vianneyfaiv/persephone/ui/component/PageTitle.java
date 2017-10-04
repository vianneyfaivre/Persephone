package re.vianneyfaiv.persephone.ui.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import re.vianneyfaiv.persephone.domain.Application;

/**
 * Page title based on current application
 */
public class PageTitle extends HorizontalLayout {

	public PageTitle(Application app, String subtitle) {
		String title = String.format("<h2>%s (%s): %s</h2>", app.getName(), app.getEnvironment(), subtitle);
		this.addComponent(new Label(title, ContentMode.HTML));
	}
}
