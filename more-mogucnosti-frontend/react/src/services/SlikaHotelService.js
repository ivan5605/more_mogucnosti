import axios from "axios";

const REST_API_BASE_URL = 'http://localhost:8080/api/v1/slikaHotel'

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

export const addSlikaH = (idHotel, slikaDto) => api.post(REST_API_BASE_URL + `/admin/create/${idHotel}`, slikaDto);

export const deleteSlikaH = (idSlika) => api.delete(REST_API_BASE_URL + `/admin/delete/${idSlika}`);

export const setGlavnaH = (idSlika) => api.put(REST_API_BASE_URL + `/admin/setGlavna/${idSlika}`);