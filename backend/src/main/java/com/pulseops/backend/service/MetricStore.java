package com.pulseops.backend.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MetricStore {
    public static class MetricPoint {
        public long timestamp;
        public double cpu;
        public double memory;
        public double disk;

        public MetricPoint(long timestamp, double cpu, double memory, double disk) {
            this.timestamp = timestamp;
            this.cpu = cpu;
            this.memory = memory;
            this.disk = disk;
        }
    }

    private final LinkedList<MetricPoint> history = new LinkedList<>();
    private final int MAX_SIZE = 60;

    public void add(MetricPoint point) {
        history.add(point);
        if (history.size() > MAX_SIZE) {
            history.removeFirst();
        }
    }

    public List<MetricPoint> getHistory() {
        return history;
    }
}
