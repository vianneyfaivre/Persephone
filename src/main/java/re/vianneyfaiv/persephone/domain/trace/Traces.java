package re.vianneyfaiv.persephone.domain.trace;

import java.util.List;

/**
 * Mapper for /trace endpoint (Actuator v2 only)
 */
public class Traces {

	private List<Trace> traces;

	public List<Trace> getTraces() {
		return traces;
	}

	@Override
	public String toString() {
		return "Traces [traces=" + traces + "]";
	}
}
