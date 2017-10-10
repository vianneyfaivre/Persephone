package re.vianneyfaiv.persephone.domain;

import org.springframework.util.StringUtils;

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
