package re.vianneyfaiv.persephone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.domain.Application;

public class ErrorHandler {

	public static ApplicationRuntimeException handle(Application app, String url, RestClientException ex) {
		if(ex instanceof HttpClientErrorException && ((HttpClientErrorException)ex).getStatusCode() == HttpStatus.NOT_FOUND) {
			return new ApplicationRuntimeException(app, String.format("Endpoint %s is not available.", url));
		} else {
			return new ApplicationRuntimeException(app, ex.getMessage());
		}
	}

	public static ApplicationRuntimeException handle(Application app, Exception ex) {
		return new ApplicationRuntimeException(app, ex.getMessage());
	}

}
