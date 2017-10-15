package re.vianneyfaiv.persephone.ui.component;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.actuate.health.Status;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class HealthCard extends VerticalLayout {
	
	private HealthCard(String title) {
		Label titleLabel = new Label(String.format("%s", title), ContentMode.HTML);
		titleLabel.setStyleName("health-card-title");
		this.addComponent(titleLabel);

		this.setStyleName("health-card");
	}

	public HealthCard(String title, Status status, String... infos) {
		this(title + ": " + status.getCode());
		Arrays.asList(infos).stream().forEach(info -> this.addComponent(new Label(info)));
	}
	
	public HealthCard(String title, String... infos) {
		this(title);
		Arrays.asList(infos).stream().forEach(info -> this.addComponent(new Label(info)));
	}
	
	public HealthCard(String title, List<String> infos) {
		this(title);
		infos.stream().forEach(info -> this.addComponent(new Label(info)));
	}
}
