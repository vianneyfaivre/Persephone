package re.vianneyfaiv.persephone.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for default {@link RestTemplate}
 */
@Configuration
public class DefaultRestTemplateConfig {

	public static final MediaType DEFAULT_ACCEPT_HEADER = MediaType.APPLICATION_JSON;

	@Value("${persephone.rest-template.read-timeout-seconds:}")
	private String readTimeout;

	@Value("${persephone.rest-template.connect-timeout-seconds:}")
	private String connectTimeout;

	@Autowired
	private RestErrorHandler restErrorHandler;

	@Bean
	public ClientHttpRequestFactory requestFactory() {

		// Disable auto redirect on 3xx HTTP responses
		CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();

		HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(httpClient);

		// read timeout
		if(!StringUtils.isEmpty(readTimeout)) {
			rf.setReadTimeout(Integer.valueOf(readTimeout) * 1000);
		}

		// connect timeout
		if(!StringUtils.isEmpty(connectTimeout)) {
			rf.setConnectTimeout(Integer.valueOf(connectTimeout) * 1000);
		}

		return rf;
	}

	@Bean
	public RestTemplate defaultRestTemplate(ClientHttpRequestFactory rf) {
		return this.restTemplate(rf, Arrays.asList(getDefaultAcceptHeader(), MediaType.ALL));
	}

	@Bean
	public RestTemplate headRestTemplate(ClientHttpRequestFactory rf) {
		return this.defaultRestTemplate(rf);
	}

	public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory, List<MediaType> types) {
		RestTemplate rt = new RestTemplate(requestFactory);

		// Override default error handler to consider HTTP 3xx 4xx and 5xx as errors
		rt.setErrorHandler(restErrorHandler);

		// Default HTTP 'Accept' header value will be application/json
		rt.getInterceptors().add(new AcceptRequestInterceptor(types));

		return rt;
	}

	public MediaType getDefaultAcceptHeader() {
		return DEFAULT_ACCEPT_HEADER;
	}

	static class AcceptRequestInterceptor implements ClientHttpRequestInterceptor {

		private List<MediaType> types;

		public AcceptRequestInterceptor(List<MediaType> types) {
			this.types = types;
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			request.getHeaders().setAccept(types);
			return execution.execute(request, body);
		}

	}
}
