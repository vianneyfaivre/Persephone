package re.vianneyfaiv.persephone.domain.app;

import org.springframework.util.StringUtils;

/**
 * Auth scheme used to identify if RestTemplate needs to send Auth headers or not
 */
public enum AuthScheme {

	BASIC,
	NONE;

	public static AuthScheme parse(String authScheme) {

		if(!StringUtils.isEmpty(authScheme)) {
			for(AuthScheme as : AuthScheme.values()) {
				if(as.name().equals(authScheme)) {
					return as;
				}
			}
		}

		return NONE;
	}
}
