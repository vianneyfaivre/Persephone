package re.vianneyfaiv.persephone.domain.env;

import org.springframework.boot.actuate.endpoint.mvc.ActuatorMediaTypes;
import org.springframework.http.MediaType;

public enum ActuatorVersion {

	V1(ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON),
	NOT_SUPPORTED(null);

	private MediaType version;

	private ActuatorVersion(MediaType version) {
		this.version = version;
	}

	public static ActuatorVersion parse(MediaType contentType) {
		if(contentType.getSubtype().equals(V1.version.getSubtype())) {
			return V1;
		}
		return NOT_SUPPORTED;
	}

}
