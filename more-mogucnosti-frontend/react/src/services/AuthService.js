import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/auth"

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
//config - objekt s URL-om, headerima, metodom, tijelom...
//request interceptor - funkcija koja se izvrši prije svakog slanja requesta kroz ovu api instancu

//ako token postoji, upisujem HTTP Authorization header sa Bearer shemom
//onda sve kaj šaljem preko api automatski nosi JWT

export const registracija = (registracijaRequest) => axios.post(REST_API_BASE_URL + "/register", registracijaRequest);

export const prijava = (prijavaRequest) => axios.post(REST_API_BASE_URL + "/login", prijavaRequest);

export const prijavljeni = () => api.get(REST_API_BASE_URL + "/me");

