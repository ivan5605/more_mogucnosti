import axios from "axios";

const REST_API_BASE_URL = 'http://localhost:8080/api/v1/slikaSoba'

const api = axios.create({
  baseURL: REST_API_BASE_URL
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
})

export const addSlikaS = (idSoba, slikaDto) => api.post(REST_API_BASE_URL + `/admin/create/${idSoba}`, slikaDto);

export const deleteSlikaS = (idSlika) => api.delete(REST_API_BASE_URL + `/admin/delete/${idSlika}`);

export const setGlavnaS = (idSlika) => api.put(REST_API_BASE_URL + `/admin/setGlavna/${idSlika}`);