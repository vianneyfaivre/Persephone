package re.vianneyfaiv.persephone.ui.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.ui.PersephoneViews;

/**
 * Page header based on current application.
 *
 * Contains a title and a button bar
 */
public class PageHeader extends VerticalLayout {

	/**
	 * @param components components that will be added horizontally before the back button
	 */
	public PageHeader(Application app, String subtitle, Component... components) {

		this.addComponent(getTitle(app, subtitle));
		this.addComponent(getButtons(components));
	}

	private HorizontalLayout getTitle(Application app, String subtitle) {
		Label title = new Label(String.format("<h2>%s (%s): %s</h2>", app.getName(), app.getEnvironment(), subtitle), ContentMode.HTML);

		return new HorizontalLayout(title);
	}

	private HorizontalLayout getButtons(Component... components) {
		Button backButton = new Button("Back to applications list",
											e -> getUI().getNavigator().navigateTo(PersephoneViews.APPLICATIONS));

		HorizontalLayout layout = new HorizontalLayout(components);
		layout.addComponent(backButton);
		return layout;
	}
}
