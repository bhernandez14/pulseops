package com.pulseops.backend.controller;

import com.pulseops.backend.service.MetricStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricsHistoryController {
    private final MetricStore store;

    public MetricsHistoryController(MetricStore store) {
        this.store = store;
    }

    @GetMapping("/history")
    public List<MetricStore.MetricPoint> getHistory() {
        return store.getHistory();
    }
}
