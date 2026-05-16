import { useEffect, useState } from "react";
import "./App.css";
import { stompClient } from "./websockets";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function App() {
  const [cpu, setCpu] = useState<any>(null);
  const [memory, setMemory] = useState<any>(null);
  const [disk, setDisk] = useState<any>(null);
  const [health, setHealth] = useState<string>("Loading...");
  const [history, setHistory] = useState<any[]>([]);

  const fetchHistory = async () => {
    const res = await fetch("http://localhost:8080/api/metrics/history");
    const data = await res.json();

    setHistory(
      data.map((p: any) => ({
        ...p,
        time: new Date(p.timestamp).toLocaleTimeString(),
      })),
    );
  };

  useEffect(() => {
    fetchHistory();
    stompClient.onConnect = () => {
      console.log("Connected to WebSocket");
      console.log("HISTORY:", history);
      stompClient.subscribe("/topic/metrics", (message) => {
        const data = JSON.parse(message.body);
        setCpu({ cpuUsage: data.cpuUsage });
        setMemory({ usagePercent: data.memoryUsage });
        setDisk({ usagePercent: data.diskUsage });
        setHealth(data.health);
        fetchHistory();
      });
    };
    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, []);
  return (
    <div className="container">
      <h1>PulseOps Dashboard</h1>
      <div className="grid">
        <div className="card">
          <h2>CPU</h2>
          <p>{cpu?.cpuUsage ?? "Loading..."}%</p>
        </div>
        <div className="card">
          <h2>Memory</h2>
          <p>{memory?.usagePercent ?? "Loading..."}%</p>
        </div>
        <div className="card">
          <h2>Disk</h2>
          <p>{disk?.usagePercent ?? "Loading..."}%</p>
        </div>
        <div className="card">
          <h2>System Health</h2>
          <p
            className={`health ${
              health === "HEALTHY"
                ? "healthy"
                : health === "WARN"
                  ? "warn"
                  : health === "CRITICAL"
                    ? "critical"
                    : ""
            }`}
          >
            {health}
          </p>
        </div>
      </div>
      <div className="chart-card" style={{ marginTop: 30 }}>
        <h2>CPU Usage</h2>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={history}>
            <XAxis dataKey="time" />
            <YAxis domain={[0, 100]} />
            <Tooltip />
            <Line type="monotone" dataKey="cpu" stroke="#22c55e" dot={false} />
          </LineChart>
        </ResponsiveContainer>
      </div>
      <div className="chart-card">
        <h2>Memory Usage</h2>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={history}>
            <XAxis dataKey="time" />
            <YAxis domain={[0, 100]} />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="memory"
              stroke="#f59e0b"
              dot={false}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
      <div className="chart-card">
        <h2>Disk Usage</h2>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={history}>
            <XAxis dataKey="time" />
            <YAxis domain={[0, 100]} />
            <Tooltip />
            <Line type="monotone" dataKey="disk" stroke="#3b82f6" dot={false} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
