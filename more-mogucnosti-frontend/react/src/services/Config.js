import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/v1";

export const apiPublic = axios.create({
  baseURL: REST_API_BASE_URL,
});

export const apiAuth = axios.create({
  baseURL: REST_API_BASE_URL,
  //timeout: 15000,
});

apiAuth.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config; //vraćam config inace request nejde dalje
});
//config - objekt s URL-om, headerima, metodom, tijelom...
//request interceptor - funkcija koja se izvrši prije svakog slanja requesta kroz ovu api instancu

//ako token postoji, upisujem HTTP Authorization header sa Bearer shemom
//onda sve kaj šaljem preko api automatski nosi JWT