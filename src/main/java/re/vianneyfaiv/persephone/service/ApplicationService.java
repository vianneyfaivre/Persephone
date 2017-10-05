package re.vianneyfaiv.persephone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.domain.Application;

@Service
public class ApplicationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsService.class);

	private List<Application> applications = new ArrayList<>();

	public List<Application> findAll() {
		LOGGER.debug("Get all applications");
		return this.applications;
	}

	public Optional<Application> findById(int appId) {
		LOGGER.debug("Get application with id {}", appId);
		return this.applications.stream().filter(app -> app.getId() == appId).findFirst();
	}

	public void addApplications(List<Application> apps) {
		LOGGER.debug("Adding applications: "+apps.toString());
        this.applications.addAll(apps);
    }
}
