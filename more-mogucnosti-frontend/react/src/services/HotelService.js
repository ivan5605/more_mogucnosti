import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/hotel'

const api = axios.create({
  baseURL: REST_API_BASE_URL
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getAllHoteli = () => axios.get(REST_API_BASE_URL);

export const getRandomHoteli = () => axios.get(REST_API_BASE_URL + "/random");

export const getHotel = (idHotel) => axios.get(REST_API_BASE_URL + '/' + idHotel);

export const updateHotel = (idHotel, hotelDto) => api.put(REST_API_BASE_URL + `/admin/update/${idHotel}`, hotelDto);

export const softDeleteHotel = (idHotel) => api.put(REST_API_BASE_URL + `/admin/softDelete/${idHotel}`);

export const createHotel = (hotelDto) => api.post(REST_API_BASE_URL + `/admin/create`, hotelDto);

export const aktivirajHotel = (idHotel) => api.put(REST_API_BASE_URL + `/admin/aktiviraj/${idHotel}`);