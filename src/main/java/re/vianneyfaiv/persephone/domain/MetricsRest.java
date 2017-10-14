package re.vianneyfaiv.persephone.domain;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class MetricsRest {

	private String name;
	private HttpStatus status;
	private long value;

	public MetricsRest(Map.Entry<String,Number> metric) {
		String[] parts = metric.getKey().split("\\.");

		if(parts.length >= 4) {
			try {
				HttpStatus status = HttpStatus.valueOf(Integer.valueOf(parts[2]));
				
				String name = "";
				for(int i = 3 ; i <= parts.length - 1 ; i++) {
					name += parts[i];
				}
				
				this.status = status;
				this.name = name;
				this.value = metric.getValue().longValue();
			} catch (IllegalArgumentException ignored) {
			}
		}
	}
	
	public boolean valid() {
		return name != null && status != null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
