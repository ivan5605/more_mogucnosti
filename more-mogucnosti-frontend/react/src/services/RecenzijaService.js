import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/recenzija"

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

export const getRecenzijeHotela = (idHotel) => axios.get(REST_API_BASE_URL + `/hotel/${idHotel}`);

export const upsertRecenzija = (idHotel, recenzija) => api.put(REST_API_BASE_URL + `/moja/hotel/${idHotel}`, recenzija);

export const getInfoRecenzija = (idHotel) => axios.get(REST_API_BASE_URL + `/hotel/info/${idHotel}`);

export const getRecenzijeKorisnika = () => api.get(REST_API_BASE_URL + '/korisnik');

export const getRecenzijeByIdKorisnik = (idKorisnik) => api.get(REST_API_BASE_URL + `/admin/korisnik/${idKorisnik}`);

export const deleteRecenzija = (idRecenzija) => api.delete(REST_API_BASE_URL + `/delete/${idRecenzija}`)