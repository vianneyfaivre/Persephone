package re.vianneyfaiv.persephone.exception;

import re.vianneyfaiv.persephone.domain.Application;

public class PersephoneTechnicalException extends RuntimeException {

	private Application application;

	public PersephoneTechnicalException(Application app, String message) {
		super(message);
		this.application = app;
	}

	public Application getApplication() {
		return this.application;
	}
}
