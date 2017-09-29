package re.vianneyfaiv.persephone.exception;

import re.vianneyfaiv.persephone.domain.Application;

public class PersephoneException extends Exception {

	private Application application;

	public PersephoneException(Application app, String message) {
		super(message);
		this.application = app;
	}

	public Application getApplication() {
		return this.application;
	}
}
