package re.vianneyfaiv.persephone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.domain.Application;

@Service
public class ApplicationService {

	private List<Application> applications;

	public List<Application> findAll() {
		return this.applications;
	}

	public Optional<Application> findById(int appId) {
		return this.applications.stream().filter(app -> app.getId() == appId).findFirst();
	}

	public void setApplications(List<Application> apps) {
		this.applications = apps;
	}
}
