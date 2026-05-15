package com.pulseops.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @GetMapping("/cpu")
    public Map<String, Object> getCpuUsage() {

        double load = osBean.getCpuLoad() * 100;

        Map<String, Object> response = new HashMap<>();
        response.put("cpuUsage", Math.round(load * 100.0) / 100.0);

        return response;
    }

    @GetMapping("/memory")
    public Map<String, Object> getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;

        double usagePercent = ((double) used / total) * 100;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalMemory", total);
        response.put("freeMemory", free);
        response.put("usedMemory", used);
        response.put("usagePercent", Math.round(usagePercent * 100.0) / 100.0);
        return response;
    }

    @GetMapping("/disk")
    public Map<String, Object> getDiskUsage() {
        java.io.File root = new java.io.File("/");

        long total = root.getTotalSpace();
        long free = root.getFreeSpace();
        long usable = root.getUsableSpace();
        long used = total - free; 
        double usagePercent = ((double) used / total) * 100;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalSpace", total);
        response.put("freeSpace", free);
        response.put("usableSpace", usable);
        response.put("usedSpace", used);
        response.put("usagePercent", Math.round(usagePercent * 100.0) / 100.0);

        return response;
    }
    
}

