package re.vianneyfaiv.persephone.ui.page;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.components.grid.ItemClickListener;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.ui.fragment.ApplicationDetailsPanel;

public class ApplicationsPage extends HorizontalLayout implements View {

	private Grid<Application> grid = new Grid<>(Application.class);
	private ApplicationDetailsPanel details;

	public ApplicationsPage(List<Application> applications) {
		this.addComponent(this.grid);
		this.grid.setCaption("Applications");
		this.grid.setItems(applications);
		this.grid.setColumns("name", "environment", "url");
		this.grid.setStyleGenerator(app -> app.isUp() ? null : "app-down");
	}

	public void setApplicationClickListener(ItemClickListener<Application> clickListener) {
		this.grid.addItemClickListener(clickListener);
	}

	public void resetDetailsFragment() {
		if(this.details != null) {
			this.removeComponent(this.details);
		}
	}

	public void updateView(Environment env) {
		this.resetDetailsFragment();

		this.details = new ApplicationDetailsPanel(env);
		this.addComponent(this.details);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
