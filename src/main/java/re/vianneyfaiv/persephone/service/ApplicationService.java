package re.vianneyfaiv.persephone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.domain.Application;

@Service
public class ApplicationService {

	private List<Application> applications = new ArrayList<>();

	public List<Application> findAll() {
		return this.applications;
	}

	public Optional<Application> findById(int appId) {
		return this.applications.stream().filter(app -> app.getId() == appId).findFirst();
	}

	public void addApplications(List<Application> apps) {
        this.applications.addAll(apps);
    }
}
