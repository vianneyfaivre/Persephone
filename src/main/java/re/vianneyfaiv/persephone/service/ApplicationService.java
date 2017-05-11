package re.vianneyfaiv.persephone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import re.vianneyfaiv.persephone.domain.Application;

@Service
public class ApplicationService {

	@Value("#{'${persephone.applications}'.split(',')}")
	private List<String> applications;

	public List<Application> findAll() {
		return this.applications
				.stream()
				.map(Application::new)
				.collect(Collectors.toList());
	}
}
