package re.vianneyfaiv.persephone.ui.component;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.actuate.health.Status;
import org.springframework.util.StringUtils;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Display a title and some info in a small bordered box
 */
public class Card extends VerticalLayout {

	private Card(String title) {
		Label titleLabel = new Label(String.format("%s", title), ContentMode.HTML);
		titleLabel.setStyleName("persephone-card-title");
		this.addComponent(titleLabel);

		this.setStyleName("persephone-card");
	}

	public Card(String title, Status status, String... infos) {
		this(title + ": " + status.getCode(), Arrays.asList(infos));
	}

	public Card(String title, String... infos) {
		this(title, Arrays.asList(infos));
	}

	public Card(String title, List<String> infos) {
		this(title);
		this.addComponent(getInfoLabels(infos));
	}

	private VerticalLayout getInfoLabels(List<String> infos) {
		VerticalLayout layout = new VerticalLayout();

		infos.stream()
			 .filter(i -> !StringUtils.isEmpty(i))
			 .forEach(info -> layout.addComponent(new Label(info)));

		layout.setStyleName("persephone-card-label");
		layout.setMargin(false);

		return layout;
	}
}
