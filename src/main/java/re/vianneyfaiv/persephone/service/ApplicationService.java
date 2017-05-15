package re.vianneyfaiv.persephone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.domain.Application;

@Service
public class ApplicationService {

	private List<Application> applications = new ArrayList<>();

	public List<Application> findAll() {
		return this.applications;
	}

	public void addApplication(Application app) {
		this.applications.add(app);
	}
}
