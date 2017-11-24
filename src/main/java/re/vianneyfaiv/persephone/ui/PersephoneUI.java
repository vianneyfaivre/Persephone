package re.vianneyfaiv.persephone.ui;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.session.UserData;
import re.vianneyfaiv.persephone.exception.UIErrorHandler;

/**
 * Vaadin UI initializer
 */
@Title("Persephone")
@Theme("persephone")
@SpringUI
@SpringViewDisplay
public class PersephoneUI extends UI implements ViewDisplay {

	private Panel springViewDisplay;

	@Value("${info.persephone.version}")
	private String persephoneVersion;

	private UserData userData = new UserData();

	@Override
	protected void init(VaadinRequest request) {

		// Root layout
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();

        root.setSpacing(false);
        root.setMargin(false);

        setContent(root);

        // Main panel
        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();

        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1);

        // Footer
        Layout footer = getFooter();
        root.addComponent(footer);
        root.setExpandRatio(footer, 0);

        // Error handler
		UI.getCurrent().setErrorHandler(new UIErrorHandler());
	}

	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}

	public UserData getUserData() {
		return userData;
	}

	private Layout getFooter() {
		Layout footer = new HorizontalLayout();

		footer.addComponent(new Label("Persephone v"+persephoneVersion));
		footer.addComponent(new Link("Created by Vianney FAIVRE", new ExternalResource("https://vianneyfaiv.re"), "_blank", 0, 0, BorderStyle.DEFAULT));
		footer.addComponent(new Link("GitHub", new ExternalResource("https://github.com/vianneyfaivre/Persephone"), "_blank", 0, 0, BorderStyle.DEFAULT));

		footer.setHeight(15, Unit.PIXELS);
		footer.setStyleName("persephone-footer");
		return footer;
	}
}
