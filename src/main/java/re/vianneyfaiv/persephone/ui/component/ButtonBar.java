package re.vianneyfaiv.persephone.ui.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import re.vianneyfaiv.persephone.ui.PersephoneViews;

/**
 * Horizontal button bar that have at least a back to applications button
 */
public class ButtonBar extends HorizontalLayout {

	public ButtonBar(Button... buttons) {
		Button backButton = new Button("Back to applications list",
											e -> getUI().getNavigator().navigateTo(PersephoneViews.APPLICATIONS));

		this.addComponents(buttons);
		this.addComponent(backButton);
	}
}
