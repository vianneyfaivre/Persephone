package re.vianneyfaiv.persephone.exception;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Exception thrown when an HTTP 3xx is received.
 *
 * Those HTTP statuses are handled as errors in order to inform user that an Actuator endpoint tried to send a redirection
 * (it often means that authentification is required for that endpoint)
 */
public class HttpRedirectErrorException extends HttpStatusCodeException {

	private static final long serialVersionUID = 6338405670035326036L;

	public HttpRedirectErrorException(HttpStatus statusCode) {
		super(statusCode);
	}

	public HttpRedirectErrorException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

	public HttpRedirectErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
		super(statusCode, statusText, responseBody, responseCharset);
	}

	public HttpRedirectErrorException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
		super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}

}
