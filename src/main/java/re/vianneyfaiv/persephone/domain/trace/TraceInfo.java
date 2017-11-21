package re.vianneyfaiv.persephone.domain.trace;

import org.springframework.http.HttpMethod;

/**
 * Mapper for /trace endpoint : HTTP request infos
 */
public class TraceInfo {

	private HttpMethod method;
	private String path;
	private TraceInfoHeaders headers;
	private long timeTaken;

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public TraceInfoHeaders getHeaders() {
		return headers;
	}

	public long getTimeTaken() {
		return timeTaken;
	}

	@Override
	public String toString() {
		return "TraceInfo [method=" + method + ", path=" + path + ", headers=" + headers + ", timeTaken=" + timeTaken
				+ "]";
	}
}
