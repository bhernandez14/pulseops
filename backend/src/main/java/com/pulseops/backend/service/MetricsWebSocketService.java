package com.pulseops.backend.service;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MetricsWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    //private final MetricStore store = new MetricStore();
    private final MetricStore store;

    public MetricsWebSocketService(
            SimpMessagingTemplate messagingTemplate,
            MetricStore store
    ) {
        this.messagingTemplate = messagingTemplate;
        this.store = store;
    }
    private final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    
    private String calculateHealth(double cpu, double memory, double disk) {
        if (cpu > 80 || memory > 85 || disk > 90) {
            return "CRITICAL";
        } else if (cpu > 60 || memory > 70 || disk > 80) {
            return "WARN";
        } else {
            return "HEALTHY";
        }
    }
    @Scheduled(fixedRate = 3000)
    public void sendMetrics() {

        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double memoryPercent = ((double) usedMemory / totalMemory) * 100;

        File root = new File("/");
        
        long totalDisk = root.getTotalSpace();
        long freeDisk = root.getFreeSpace();
        long usedDisk = totalDisk - freeDisk; 
        double diskPercent = ((double) usedDisk / totalDisk) * 100;

        double cpu = Math.round(osBean.getCpuLoad() * 10000.0) / 100.0;

        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("cpuUsage", cpu);
        metrics.put("memoryUsage",
                Math.round(memoryPercent * 100.0) / 100.0);
        metrics.put("diskUsage",
                Math.round(diskPercent * 100.0) / 100.0);
        metrics.put("health", calculateHealth(cpu, memoryPercent, diskPercent));

        messagingTemplate.convertAndSend("/topic/metrics", metrics);
        store.add(new MetricStore.MetricPoint(System.currentTimeMillis(), cpu, memoryPercent, diskPercent));
    }

}
