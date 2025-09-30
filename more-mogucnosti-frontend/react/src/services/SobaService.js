import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/v1/soba';

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

export const getSoba = (idSoba) => axios.get(REST_API_BASE_URL + '/' + idSoba);

export const getSobeHotela = (idHotel) => axios.get(REST_API_BASE_URL + '/hotel/' + idHotel);

export const getSobaWithHotelAndSlike = (idSoba) => axios.get(REST_API_BASE_URL + '/withHotelAndSlike/' + idSoba);

export const addSoba = (idHotel, sobaDto) => api.post(REST_API_BASE_URL + `/admin/create/${idHotel}`, sobaDto);

export const updateSoba = (idSoba, sobaDto) => api.put(REST_API_BASE_URL + `/admin/update/${idSoba}`, sobaDto);

export const softDeleteSoba = (idSoba) => api.put(REST_API_BASE_URL + `/admin/softDelete/${idSoba}`);

export const aktivirajSoba = (idSoba) => api.put(REST_API_BASE_URL + `/admin/aktiviraj/${idSoba}`);