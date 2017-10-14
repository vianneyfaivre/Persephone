package re.vianneyfaiv.persephone.exception;

public class ApplicationNotFoundException extends RuntimeException {

	public ApplicationNotFoundException(int appId) {
		super(String.format("Application with id %s has not been found", appId));
	}
}
