import axios from "axios";

const API = "http://localhost:8080/api/metrics";

export const getCpu = async () => {
  const res = await axios.get(`${API}/cpu`);
  return res.data;
};

export const getMemory = async () => {
  const res = await axios.get(`${API}/memory`);
  return res.data;
};

export const getDisk = async () => {
  const res = await axios.get(`${API}/disk`);
  return res.data;
};
