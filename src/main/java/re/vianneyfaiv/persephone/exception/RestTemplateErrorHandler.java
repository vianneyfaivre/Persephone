package re.vianneyfaiv.persephone.exception;

import org.springframework.http.HttpStatus.Series;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.domain.app.Application;

/**
 * Error handler for all calls to Actuator endpoints, will handle HTTP 3xx/4xx/5xx statuses
 */
public class RestTemplateErrorHandler {

	/**
	 * Handles HTTP 3xx/4xx/5xx statuses
	 */
	public static ApplicationRuntimeException handle(Application app, String url, RestClientException ex) {

		// HTTP 5xx
		if(ex instanceof HttpServerErrorException) {
			HttpServerErrorException serverEx = ((HttpServerErrorException)ex);
			return new ApplicationRuntimeException(app, String.format("A server error happened while calling %s (Got HTTP %s)", url, serverEx.getRawStatusCode()));
		}
		// HTTP 4xx
		else if(ex instanceof HttpClientErrorException) {
			HttpClientErrorException clientEx = ((HttpClientErrorException)ex);
			return new ApplicationRuntimeException(app, String.format("Endpoint %s is not available (Got HTTP %s)", url, clientEx.getRawStatusCode()));
		}
		// HTTP 3xx
		else if(ex instanceof HttpRedirectErrorException) {

			HttpRedirectErrorException redirectEx = ((HttpRedirectErrorException)ex);

			if(redirectEx.getStatusCode().series() == Series.REDIRECTION) {
				return new ApplicationRuntimeException(app, String.format("Endpoint %s is available but security might be enabled (Got HTTP %s)", url, redirectEx.getRawStatusCode() ));
			}
		}

		return handle(app, ex);
	}

	public static ApplicationRuntimeException handle(Application app, Exception ex) {
		return new ApplicationRuntimeException(app, ex.getMessage());
	}

}
