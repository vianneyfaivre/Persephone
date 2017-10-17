package re.vianneyfaiv.persephone.ui.component;

import java.time.Duration;
import java.util.Date;

import org.springframework.http.HttpMethod;

import re.vianneyfaiv.persephone.domain.trace.Trace;

public class TraceGridRow {

	private Date timestamp;
	private HttpMethod method;
	private String path;
	private Duration timeTaken;

	public TraceGridRow(Trace trace) {
		this.timestamp = new Date(trace.getTimestamp());
		this.method = trace.getInfo().getMethod();
		this.path = trace.getInfo().getPath();
		this.timeTaken = Duration.ofMillis(trace.getInfo().getTimeTaken());
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

}
