package re.vianneyfaiv.persephone.domain.env;

import org.springframework.boot.actuate.endpoint.mvc.ActuatorMediaTypes;
import org.springframework.http.MediaType;

public enum ActuatorVersion {

	V1(ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON_VALUE),
	V2("application/vnd.spring-boot.actuator.v2+json");

	private String mediaType;

	private ActuatorVersion(String mediaType) {
		this.mediaType = mediaType;
	}

	public static ActuatorVersion parse(MediaType contentType) {

		String simpleType = contentType.getType() + "/" + contentType.getSubtype();

		if(V1.mediaType.equals(simpleType)) {
			return V1;
		}

		if(V2.mediaType.equals(simpleType)) {
			return V2;
		}

		// By default, consider it's Actuator from spring boot 1.x
		return V1;
	}

}
