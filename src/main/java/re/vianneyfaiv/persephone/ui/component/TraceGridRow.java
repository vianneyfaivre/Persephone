package re.vianneyfaiv.persephone.ui.component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpMethod;

import re.vianneyfaiv.persephone.domain.trace.Trace;

public class TraceGridRow {

	private Date timestamp;
	private HttpMethod method;
	private String path;
	private Duration timeTaken;
	private Map<String, String> requestHeaders;
	private Map<String, String> responseHeaders;

	public TraceGridRow(Trace trace) {
		this.timestamp = new Date(trace.getTimestamp());
		this.method = trace.getInfo().getMethod();
		this.path = trace.getInfo().getPath();
		this.timeTaken = Duration.ofMillis(trace.getInfo().getTimeTaken());
		this.requestHeaders = trace.getInfo().getHeaders().getRequest();
		this.responseHeaders = trace.getInfo().getHeaders().getResponse();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Duration getTimeTaken() {
		return timeTaken;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

}
