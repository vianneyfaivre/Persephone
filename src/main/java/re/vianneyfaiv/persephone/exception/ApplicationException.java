package re.vianneyfaiv.persephone.exception;

import re.vianneyfaiv.persephone.domain.app.Application;

/**
 * Thrown when an expected error happens
 */
public class ApplicationException extends Exception {

	private Application application;

	public ApplicationException(Application app, String message) {
		super(message);
		this.application = app;
	}

	public Application getApplication() {
		return this.application;
	}
}
