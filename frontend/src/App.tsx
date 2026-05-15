import { useEffect, useState } from "react";
import { getCpu, getMemory, getDisk } from "./api/metrics";
import "./App.css";

export default function App() {
  const [cpu, setCpu] = useState<any>(null);
  const [memory, setMemory] = useState<any>(null);
  const [disk, setDisk] = useState<any>(null);

  const fetchData = async () => {
    try {
      const cpuRes = await getCpu();
      const memRes = await getMemory();
      const diskRes = await getDisk();

      setCpu(cpuRes);
      setMemory(memRes);
      setDisk(diskRes);

      console.log("CPU:", cpuRes);
      console.log("MEM:", memRes);
      console.log("DISK:", diskRes);
    } catch (err) {
      console.error("Error fetching metrics:", err);
    }
  };
  useEffect(() => {
    fetchData();

    const interval = setInterval(() => {
      fetchData();
    }, 3000);

    return () => clearInterval(interval);
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
      </div>
    </div>
  );
}
