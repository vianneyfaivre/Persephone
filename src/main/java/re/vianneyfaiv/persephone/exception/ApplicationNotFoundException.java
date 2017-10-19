package re.vianneyfaiv.persephone.exception;

/**
 * Thrown when an application has not been found
 */
public class ApplicationNotFoundException extends RuntimeException {

	public ApplicationNotFoundException(int appId) {
		super(String.format("Application with id %s has not been found", appId));
	}
}
