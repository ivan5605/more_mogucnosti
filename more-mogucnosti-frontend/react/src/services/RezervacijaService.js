import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/rezervacija"

const api = axios.create({
  baseURL: REST_API_BASE_URL
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config; //vraćam config inace request nejde dalje
})

export const createRezervacija = (rezervacija) => api.post(REST_API_BASE_URL, rezervacija);

export const getZauzetiDatumi = (idSoba) => api.get(REST_API_BASE_URL + `/datumi/${idSoba}`);

export const getRezervacijeKorisnika = () => api.get(REST_API_BASE_URL + '/korisnik');