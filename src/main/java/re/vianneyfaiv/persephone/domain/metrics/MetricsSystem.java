package re.vianneyfaiv.persephone.domain.metrics;

import java.time.Duration;
import java.util.Collection;

/**
 * Mapper for /metrics endpoint : system
 */
public class MetricsSystem {

   private long heap;
   private long heapCommitted;
   private long heapInit;
   private long heapUsed;

   private long mem;
   private long memAllocated;
   private long memFree;

   private int processors;
   private Duration uptime;
   private Duration instanceUptime;
   private double systemLoadAverage;

   private int threads;
   private int threadPeak;
   private int threadDaemon;

   private int classes;
   private int classesLoaded;
   private int classesUnloaded;

   private Collection<MetricsGc> garbageCollectionInfos;

   public MetricsSystem(long heap, long heapCommitted, long heapInit, long heapUsed, long mem, long memFree, int processors, Duration uptime, Duration instanceUptime, double systemLoadAverage, int threads, int threadPeak, int threadDaemon, int classes, int classesLoaded, int classesUnloaded, Collection<MetricsGc> garbageCollectionInfos) {
      this.heap = heap;
      this.heapCommitted = heapCommitted;
      this.heapInit = heapInit;
      this.heapUsed = heapUsed;
      this.mem = mem;
      this.memFree = memFree;
      this.memAllocated = this.mem - this.memFree;
      this.processors = processors;
      this.uptime = uptime;
      this.instanceUptime = instanceUptime;
      this.systemLoadAverage = systemLoadAverage;
      this.threads = threads;
      this.threadPeak = threadPeak;
      this.threadDaemon = threadDaemon;
      this.classes = classes;
      this.classesLoaded = classesLoaded;
      this.classesUnloaded = classesUnloaded;
      this.garbageCollectionInfos = garbageCollectionInfos;
   }

   public long getHeap() {
      return heap;
   }

   public long getHeapCommitted() {
      return heapCommitted;
   }

   public long getHeapCommittedUsedPercentage() {
      return (100 * this.heapUsed) / this.heapCommitted;
   }

   public long getHeapCommittedFreePercentage() {
      return 100 - getHeapCommittedUsedPercentage();
   }

   public long getHeapInit() {
      return heapInit;
   }

   public long getHeapUsed() {
      return heapUsed;
   }

   public long getMem() {
      return mem;
   }

   public long getMemAllocated() {
      return memAllocated;
   }

   public long getMemFree() {
      return memFree;
   }

   public long getMemFreePercentage() {
      return (100 * this.memFree) / this.mem;
   }

   public long getMemInUsePercentage() {
      return (100 * this.mem) / this.memFree;
   }

   public int getProcessors() {
      return processors;
   }

   public Duration getUptime() {
      return uptime;
   }

   public Duration getInstanceUptime() {
      return instanceUptime;
   }

   public double getSystemLoadAverage() {
      return systemLoadAverage;
   }

   public int getThreads() {
      return threads;
   }

   public int getThreadPeak() {
      return threadPeak;
   }

   public int getThreadDaemon() {
      return threadDaemon;
   }

   public int getClasses() {
      return classes;
   }

   public int getClassesLoaded() {
      return classesLoaded;
   }

   public int getClassesUnloaded() {
      return classesUnloaded;
   }

   public Collection<MetricsGc> getGarbageCollectionInfos() {
      return garbageCollectionInfos;
   }

}