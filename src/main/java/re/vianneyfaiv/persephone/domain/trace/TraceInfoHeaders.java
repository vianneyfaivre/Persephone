package re.vianneyfaiv.persephone.domain.trace;

import java.util.List;
import java.util.Map;

/**
 * Mapper for /trace endpoint : HTTP Headers request/response
 */
public class TraceInfoHeaders {

	private Map<String, List<String>> request;
	private Map<String, List<String>> response;

	public TraceInfoHeaders(Map<String, List<String>> request, Map<String, List<String>> response) {
		this.request = request;
		this.response = response;
	}

	public Map<String, List<String>> getRequest() {
		return request;
	}

	public Map<String, List<String>> getResponse() {
		return response;
	}
}
