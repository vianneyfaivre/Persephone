package re.vianneyfaiv.persephone.ui.component.grid;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import re.vianneyfaiv.persephone.domain.trace.Trace;

public class TraceGridRow {

	private Date timestamp;
	private HttpMethod method;
	private String path;
	private Optional<Duration> timeTaken;
	private Optional<HttpStatus> responseHttp;
	private Map<String, List<String>> requestHeaders;
	private Map<String, List<String>> responseHeaders;

	public TraceGridRow(Trace trace) {
		this.timestamp = new Date(trace.getTimestamp());

		if(trace.getInfo() != null) {
			this.method = trace.getInfo().getMethod();
			this.path = trace.getInfo().getPath();

			if(trace.getInfo().getTimeTaken() > 0) {
				this.timeTaken = Optional.of(Duration.ofMillis(trace.getInfo().getTimeTaken()));
			} else {
				// old versions of spring boot does not provide that info
				this.timeTaken = Optional.empty();
			}

			if(trace.getInfo().getHeaders() != null) {

				if(trace.getInfo().getHeaders().getRequest() != null) {
					this.requestHeaders = trace.getInfo().getHeaders().getRequest();
				} else {
					this.requestHeaders = new HashMap<>();
				}

				if(trace.getInfo().getHeaders().getResponse() != null) {
					this.responseHeaders = trace.getInfo().getHeaders().getResponse();
				} else {
					this.responseHeaders = new HashMap<>();
				}

				List<String> statusList = this.responseHeaders.getOrDefault("status", new ArrayList<>());
				if(!statusList.isEmpty() && !StringUtils.isEmpty(statusList.get(0))) {
					this.responseHttp = Optional.of(HttpStatus.valueOf(Integer.valueOf(statusList.get(0))));
				} else {
					this.responseHttp = Optional.empty();
				}
			}

		}

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

	public Optional<Duration> getTimeTaken() {
		return timeTaken;
	}

	public Map<String, List<String>> getRequestHeaders() {
		return requestHeaders;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	public Optional<HttpStatus> getResponseHttp() {
		return responseHttp;
	}

}
