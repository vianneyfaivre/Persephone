package re.vianneyfaiv.persephone.service;

import re.vianneyfaiv.persephone.domain.Application;

public class PersephoneServiceException extends RuntimeException {

	private Application application;

	public PersephoneServiceException(Application app, String message) {
		super(message);
		this.application = app;
	}

	public Application getApplication() {
		return this.application;
	}
}
