package re.vianneyfaiv.persephone.ui.page;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.components.grid.ItemClickListener;

import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Environment;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.ui.fragment.ApplicationOverviewPanel;

/**
 * Page that lists applications.
 *
 * When selecting an application, a details panel will be displayed.
 *
 * {@link ApplicationOverviewPanel}
 */
public class ApplicationsPage extends HorizontalLayout implements View {

	private Grid<Application> grid = new Grid<>(Application.class);
	private ApplicationOverviewPanel details;

	public ApplicationsPage(List<Application> applications) {
		this.addComponent(this.grid);
		this.grid.setCaption("<h2>Applications</h2>");
		this.grid.setCaptionAsHtml(true);
		this.grid.setItems(applications);
		this.grid.setColumns("name", "environment", "url");
		this.grid.setStyleGenerator(app -> app.isUp() ? null : "app-down");
	}

	/**
	 * Sets application.onClick
	 */
	public void setApplicationClickListener(ItemClickListener<Application> clickListener) {
		this.grid.addItemClickListener(clickListener);
	}

	/**
	 * Removes the details panel
	 */
	public void resetDetailsFragment() {
		if(this.details != null) {
			this.removeComponent(this.details);
		}
	}

	/**
	 * Updates the whole component
	 */
	public void updateView(Application app, Environment env, Metrics metrics) {
		this.resetDetailsFragment();

		this.details = new ApplicationOverviewPanel(app, env, metrics);
		this.addComponent(this.details);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
