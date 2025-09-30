import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/v1/korisnik';

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

export const addKorisnik = (korisnik) => axios.post(REST_API_BASE_URL, korisnik);

export const getKorisniciWithCount = () => api.get(REST_API_BASE_URL + '/admin/count');

export const getKorisnik = (idKorisnik) => axios.get(REST_API_BASE_URL + `/${idKorisnik}`);

export const deleteKorisnik = (lozinka) => api.delete(REST_API_BASE_URL + '/delete', {
  data: lozinka,
  headers: { 'Content-Type': 'text/plain' }
});

export const updateKorisnik = (azuriraniKor) => api.put(REST_API_BASE_URL + "/update", azuriraniKor);

export const updateLozinka = (promjenaLozinkaDto) => api.put(REST_API_BASE_URL + "/updateLozinka", promjenaLozinkaDto);

export const adminDeleteKorisnik = (idKorisnik) => api.delete(REST_API_BASE_URL + `/admin/delete/${idKorisnik}`)