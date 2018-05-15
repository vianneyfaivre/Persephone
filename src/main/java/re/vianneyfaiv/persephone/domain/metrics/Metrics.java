package re.vianneyfaiv.persephone.domain.metrics;

import java.time.Duration;

/**
 * Mapper for /metrics endpoint
 */
public class Metrics {

   private long mem;
   private long memAllocated;
   private long memFree;
   private long uptime;
   private int httpSessionsActive;

   public Metrics(long mem, long memFree, long uptime, int httpSessionsActive) {
      this.mem = mem;
      this.memFree = memFree;
      this.memAllocated = this.mem - this.memFree;
      this.uptime = uptime;
      this.httpSessionsActive = httpSessionsActive;
   }

   public long getMemFreePercentage() {
      return (100 * this.memFree) / this.mem;
   }

   public Duration getUptime() {
      return Duration.ofMillis(this.uptime);
   }

   public long getMem() {
      return this.mem;
   }

   public long getMemAllocated() {
      return this.memAllocated;
   }

   public long getMemFree() {
      return this.memFree;
   }

   public int getHttpSessionsActive() {
      return this.httpSessionsActive;
   }

   @Override
   public String toString() {
      return "Metrics [mem=" + this.mem + ", memFree=" + this.memFree + ", uptime="
            + this.uptime + ", httpSessionsActive=" + this.httpSessionsActive + "]";
   }
}