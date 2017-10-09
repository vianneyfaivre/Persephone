package re.vianneyfaiv.persephone.ui.util;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Formatters {

	public static String readableDuration(Duration uptime) {
		long seconds = uptime.get(ChronoUnit.SECONDS);

		if(seconds < 60) {
			return seconds+" sec";
		} else {
			long minutes = uptime.toMinutes();
			if(minutes < 60) {
				return minutes+" min";
			} else {
				long hours = uptime.toHours();
				if(hours < 24) {
					return hours +" hours";
				}
				else {
					long days = uptime.toDays();
					return days + " days";
				}
			}
		}
	}

	public static String readableFileSize(long bytesSize) {
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(bytesSize)/Math.log10(1000));
	    return new DecimalFormat("#,##0").format(bytesSize/Math.pow(1000, digitGroups)) + " " + units[digitGroups];
	}
}
