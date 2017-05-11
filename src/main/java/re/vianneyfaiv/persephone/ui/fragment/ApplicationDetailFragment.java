package re.vianneyfaiv.persephone.ui.fragment;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import re.vianneyfaiv.persephone.domain.Environment;

public class ApplicationDetailFragment extends VerticalLayout implements View {

	public ApplicationDetailFragment(Environment env) {
		this.addComponent(this.getField("Profiles", env.getProfiles().toString()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private HorizontalLayout getField(String label, String value) {
		return new HorizontalLayout(new Label(label), new Label(value));
	}

}

