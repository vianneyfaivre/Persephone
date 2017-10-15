package re.vianneyfaiv.persephone.domain.metrics;

import java.time.Duration;
import java.util.Collection;

public class MetricsSystem {

	private int heap;
	private int heapCommitted;
	private int heapInit;
	private int heapUsed;

	private int mem;
	private int memAllocated;
	private int memFree;

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

	public MetricsSystem(int heap, int heapCommitted, int heapInit, int heapUsed, int mem, int memFree, int processors, Duration uptime, Duration instanceUptime, double systemLoadAverage, int threads, int threadPeak, int threadDaemon, int classes, int classesLoaded, int classesUnloaded, Collection<MetricsGc> garbageCollectionInfos) {
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

	public int getHeap() {
		return heap;
	}

	public int getHeapCommitted() {
		return heapCommitted;
	}
	
	public int getHeapCommittedUsedPercentage() {
		return (100 * this.heapUsed) / this.heapCommitted;
	}
	
	public int getHeapCommittedFreePercentage() {
		return 100 - getHeapCommittedUsedPercentage();
	}

	public int getHeapInit() {
		return heapInit;
	}

	public int getHeapUsed() {
		return heapUsed;
	}
	
	public int getMem() {
		return mem;
	}

	public int getMemAllocated() {
		return memAllocated;
	}

	public int getMemFree() {
		return memFree;
	}
	
	public int getMemFreePercentage() {
		return (100 * this.memFree) / this.mem;
	}
	
	public int getMemInUsePercentage() {
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
