package re.vianneyfaiv.persephone.ui.component;

import java.util.Arrays;

import org.springframework.boot.actuate.health.Status;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class HealthCard extends VerticalLayout {

	public HealthCard(String title, Status status, String... infos) {
		this.addComponent(new Label(String.format("Health: %s", title), ContentMode.HTML));

		Arrays.asList(infos).stream()
			  .forEach(info -> this.addComponent(new Label(info)));
	}
}
