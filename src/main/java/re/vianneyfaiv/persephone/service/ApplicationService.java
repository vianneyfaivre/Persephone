package re.vianneyfaiv.persephone.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.bootstrap.BatchReadApplicationsFromCsv;
import re.vianneyfaiv.persephone.domain.app.Application;

/**
 * Service that holds all applications (loaded from {@link BatchReadApplicationsFromCsv})
 */
@Service
public class ApplicationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsService.class);

	private List<Application> applications = Collections.emptyList();

	public List<Application> findAll() {
		LOGGER.debug("Get all applications");
		return this.applications;
	}

	public Optional<Application> findById(int appId) {
		LOGGER.debug("Get application with id {}", appId);
		return this.applications.stream().filter(app -> app.getId() == appId).findFirst();
	}

	public void setApplications(List<Application> apps) {
		LOGGER.debug("Adding {} applications", apps.size());
        this.applications = Collections.unmodifiableList(apps);
    }

	public void setUp(Application app, boolean up) {
		if(app.isUp() != up) {
			LOGGER.debug("Changing Application#up to {} for application with id {}", up, app.getId());
			app.setUp(up);
		}
	}
}
