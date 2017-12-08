package re.vianneyfaiv.persephone.service.v1;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import re.vianneyfaiv.persephone.config.RestTemplateFactory;
import re.vianneyfaiv.persephone.domain.app.Application;
import re.vianneyfaiv.persephone.domain.metrics.Metrics;
import re.vianneyfaiv.persephone.domain.metrics.MetricsCache;
import re.vianneyfaiv.persephone.domain.metrics.MetricsDatasource;
import re.vianneyfaiv.persephone.domain.metrics.MetricsGc;
import re.vianneyfaiv.persephone.domain.metrics.MetricsRest;
import re.vianneyfaiv.persephone.domain.metrics.MetricsSystem;
import re.vianneyfaiv.persephone.exception.RestTemplateErrorHandler;

/**
 * Calls /metrics
 */
@Service
public class MetricsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

	@Autowired
	private RestTemplateFactory restTemplates;

	public Map<String, Number> getAllMetrics(Application app) {

		String url = app.endpoints().metrics();

		try {
			LOGGER.debug("GET {}", url);
			Map<String, Number> metrics = restTemplates.get(app).getForObject(url, Map.class);

			return metrics;
		} catch(RestClientException e) {
			throw RestTemplateErrorHandler.handle(app, url, e);
		}
	}

	public Metrics getMetrics(Application app) {

		Map<String, Number> allMetrics = this.getAllMetrics(app);

		int mem = allMetrics.getOrDefault("mem", -1).intValue();
		int memFree = allMetrics.getOrDefault("mem.free", -1).intValue();
		long uptime = allMetrics.getOrDefault("uptime", -1).longValue();
		int httpSessionsActive = allMetrics.getOrDefault("httpsessions.active", -1).intValue();

		return new Metrics(mem, memFree, uptime, httpSessionsActive);
	}

	public Collection<MetricsCache> getMetricsCaches(Map<String, Number> metrics) {

		Map<String, MetricsCache> metricsCache = new HashMap<>();

		metrics
			.entrySet().stream()
			.filter(metric -> metric.getKey().startsWith("cache."))
			.forEach(cacheMetric -> {

				String[] parts = cacheMetric.getKey().split("\\.");

				if(parts.length >= 3) {
			        String cacheName = parts[1]; // TODO : it can contains dots, so join elements parts.1 to parts.length-3
			        String sizePart = parts[parts.length-1];
			        String ratioType = parts[parts.length-2];

			        MetricsCache mc = metricsCache.get(cacheName);

			        if(mc == null) {
			        	mc = new MetricsCache(cacheName);
			        	metricsCache.put(cacheName, mc);
			        }

			        if("miss".equals(ratioType)) {
			        	mc.setMissRatio(cacheMetric.getValue().doubleValue());
			        } else if("hit".equals(ratioType)) {
			        	mc.setHitRatio(cacheMetric.getValue().doubleValue());
			        } else if("size".equals(sizePart)) {
			        	mc.setSize(cacheMetric.getValue().longValue());
			        }
				}
			});

		return metricsCache.values();
	}

	public List<MetricsRest> getMetricsRest(Map<String, Number> metrics) {

		// get all Rest metrics
		List<MetricsRest> metricsRest = metrics
											.entrySet().stream()
											.filter(metric -> metric.getKey().startsWith("counter.status"))
											.map(MetricsRest::new)
											.collect(Collectors.toList());


		// Get last response time
		metricsRest.stream()
			.forEach(m -> {
				Number lastResponseTime = metrics.get("gauge.response."+m.getName());

				if(lastResponseTime != null) {
					m.setLastResponseTime(lastResponseTime.intValue());
				} else {
					m.setLastResponseTime(-1);
				}
			});

		return metricsRest;
	}

	public Collection<MetricsDatasource> getMetricsDatasources(Map<String, Number> metrics) {
		// datasource.xxx.active
		// datasource.xxx.usage

		Map<String, MetricsDatasource> metricsDb = new HashMap<>();

		metrics
			.entrySet().stream()
			.filter(metric -> metric.getKey().startsWith("datasource."))
			.forEach(dbMetric -> {

				String[] parts = dbMetric.getKey().split("\\.");

				if(parts.length >= 3) {
			        String name = parts[1];
			        String metricType = parts[parts.length-1];

			        MetricsDatasource mc = metricsDb.get(name);

			        if(mc == null) {
			        	mc = new MetricsDatasource(name);
			        	metricsDb.put(name, mc);
			        }

			        if("active".equals(metricType)) {
			        	mc.setActiveConnections(dbMetric.getValue().intValue());
			        } else if("usage".equals(metricType)) {
			        	mc.setConnectionPoolUsage(dbMetric.getValue().intValue());
			        }
				}
			});

		return metricsDb.values();
	}
	
	public MetricsSystem getSystemMetrics(Map<String, Number> metrics) {
//		Heap information in KB (heap, heap.committed, heap.init, heap.used)
		int heap = metrics.getOrDefault("heap", -1).intValue();
		int heapCommitted = metrics.getOrDefault("heap.committed", -1).intValue();
		int heapInit = metrics.getOrDefault("heap.init", -1).intValue();
		int heapUsed = metrics.getOrDefault("heap.used", -1).intValue();

//		The total system memory in KB (mem)
		int mem = metrics.getOrDefault("mem", -1).intValue();
//		The amount of free memory in KB (mem.free)
		int memFree = metrics.getOrDefault("mem.free", -1).intValue();

//		The number of processors (processors)
		int processors = metrics.getOrDefault("processors", -1).intValue();
//		The system uptime in milliseconds (uptime)
		Duration uptime = Duration.ofMillis(metrics.getOrDefault("uptime", -1).longValue());
//		The application context uptime in milliseconds (instance.uptime)
		Duration instanceUptime = Duration.ofMillis(metrics.getOrDefault("instance.uptime", -1).longValue());
//		The average system load (systemload.average)
		double systemLoadAverage = metrics.getOrDefault("systemload.average", -1).doubleValue();

//		Thread information (threads, thread.peak, thread.daemon)
		int threads = metrics.getOrDefault("threads", -1).intValue();
		int threadPeak = metrics.getOrDefault("threads.peak", -1).intValue();
		int threadDaemon = metrics.getOrDefault("threads.daemon", -1).intValue();

//		Class load information (classes, classes.loaded, classes.unloaded)
		int classes = metrics.getOrDefault("classes", -1).intValue();
		int classesLoaded = metrics.getOrDefault("classes.loaded", -1).intValue();
		int classesUnloaded = metrics.getOrDefault("classes.unloaded", -1).intValue();

//		Garbage collection information (gc.xxx.count, gc.xxx.time)
		Map<String, MetricsGc> metricsGc = new HashMap<>();
		metrics
			.entrySet().stream()
			.filter(metric -> metric.getKey().startsWith("gc."))
			.forEach(gcMetric -> {

				String[] parts = gcMetric.getKey().split("\\.");

				if(parts.length >= 3) {
			        String name = parts[1];
			        String metricType = parts[parts.length-1];

			        MetricsGc mc = metricsGc.get(name);

			        if(mc == null) {
			        	mc = new MetricsGc(name);
			        	metricsGc.put(name, mc);
			        }

			        if("count".equals(metricType)) {
			        	mc.setCount(gcMetric.getValue().intValue());
			        } else if("time".equals(metricType)) {
			        	mc.setTime(Duration.ofMillis(gcMetric.getValue().longValue()));
			        }
				}
			});

		return new MetricsSystem(heap, heapCommitted, heapInit, heapUsed, mem, memFree, processors, uptime, instanceUptime, systemLoadAverage, threads, threadPeak, threadDaemon, classes, classesLoaded, classesUnloaded, metricsGc.values());
	}
}
