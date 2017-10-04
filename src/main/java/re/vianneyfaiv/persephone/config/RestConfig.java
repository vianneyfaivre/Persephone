package re.vianneyfaiv.persephone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

	@Value("${persephone.rest-template.read-timeout-seconds:}")
	private String readTimeout;

	@Value("${persephone.rest-template.connect-timeout-seconds:}")
	private String connectTimeout;

	@Bean
	public ClientHttpRequestFactory requestFactory() {
		SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();

		if(!StringUtils.isEmpty(readTimeout)) {
			System.out.println("allo");
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

		return rt;
	}
}
