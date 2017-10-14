package re.vianneyfaiv.persephone.service;

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
import re.vianneyfaiv.persephone.domain.Application;
import re.vianneyfaiv.persephone.domain.Metrics;
import re.vianneyfaiv.persephone.domain.MetricsCache;
import re.vianneyfaiv.persephone.domain.MetricsRest;
import re.vianneyfaiv.persephone.exception.ErrorHandler;

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
			throw ErrorHandler.handle(app, url, e);
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
			.filter(metric -> metric.getKey().startsWith("cache"))
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
		return metrics
			.entrySet().stream()
			.filter(metric -> metric.getKey().startsWith("counter.status"))
			.map(MetricsRest::new)
			.collect(Collectors.toList());
	}
}
