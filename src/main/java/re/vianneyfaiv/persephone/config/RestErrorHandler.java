package re.vianneyfaiv.persephone.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import re.vianneyfaiv.persephone.exception.HttpRedirectErrorException;

/**
 * Handle HTTP 3xx as errors
 */
@Configuration
public class RestErrorHandler extends DefaultResponseErrorHandler {

	@Override
	protected boolean hasError(HttpStatus statusCode) {
		return statusCode.series() == HttpStatus.Series.REDIRECTION || super.hasError(statusCode);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = getHttpStatusCode(response);

		// handle 3xx
		switch (statusCode.series()) {
			case REDIRECTION:
				throw new HttpRedirectErrorException(statusCode, response.getStatusText(),
						response.getHeaders(), getResponseBody(response), getCharset(response));
		}

		// handler 4xx and 5xx
		super.handleError(response);
	}
}
