package re.vianneyfaiv.persephone.domain.trace;

import java.util.Map;

/**
 * Mapper for /trace endpoint : HTTP Headers request/response
 */
public class TraceInfoHeaders {

	private Map<String, String> request;
	private Map<String, String> response;

	public Map<String, String> getRequest() {
		return request;
	}

	public Map<String, String> getResponse() {
		return response;
	}
}
