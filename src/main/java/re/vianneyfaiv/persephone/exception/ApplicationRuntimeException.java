package re.vianneyfaiv.persephone.exception;

import re.vianneyfaiv.persephone.domain.app.Application;

/**
 * Thrown when an unexpected error happens.
 * Contains {@link Application} so a meaningful message can be displayed to the user.
 */
public class ApplicationRuntimeException extends RuntimeException {

	private Application application;

	public ApplicationRuntimeException(Application app, String message) {
		super(message);
		this.application = app;
	}

	public Application getApplication() {
		return this.application;
	}
}
