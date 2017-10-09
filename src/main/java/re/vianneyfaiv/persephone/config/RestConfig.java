package re.vianneyfaiv.persephone.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

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

		if(!StringUtils.isEmpty(readTimeout)) {
			rf.setReadTimeout(Integer.valueOf(readTimeout) * 1000);
		}

		if(!StringUtils.isEmpty(connectTimeout)) {
			rf.setConnectTimeout(Integer.valueOf(connectTimeout) * 1000);
		}

		return rf;
	}

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
		RestTemplate rt = new RestTemplate(requestFactory);

		rt.setErrorHandler(restErrorHandler);

		return rt;
	}
}
