import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/v1/rezervacija"

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

export const createRezervacija = (rezervacija) => api.post(REST_API_BASE_URL + '/create', rezervacija);

export const getZauzetiDatumi = (idSoba) => axios.get(REST_API_BASE_URL + `/datumi/${idSoba}`);

export const getZauzetiDatumiOsim = (idSoba, idRez) => axios.get(REST_API_BASE_URL + `/datumi/${idSoba}`, {
  params: { idRez }
});

export const getRezervacijeKorisnika = () => api.get(REST_API_BASE_URL + '/korisnik');

export const getAktivneRezervacije = (idKorisnik) => api.get(REST_API_BASE_URL + `/admin/korisnikAkt/${idKorisnik}`);

export const getStareRezervacije = (idKorisnik) => api.get(REST_API_BASE_URL + `/admin/korisnikSt/${idKorisnik}`);

export const updateRezervacija = (idRezervacija, rezervacija) => api.put(REST_API_BASE_URL + `/update/${idRezervacija}`, rezervacija);

export const deleteRezervacija = (idRezervacija) => api.delete(REST_API_BASE_URL + `/delete/${idRezervacija}`);

export const adminDeleteRezervacija = (idRezervacija) => api.delete(REST_API_BASE_URL + `/admin/delete/${idRezervacija}`);

export const adminUpdateRezervacija = (idRezervacija, rezervacijaDto) => api.put(REST_API_BASE_URL + `/admin/update/${idRezervacija}`, rezervacijaDto);

export const getRezervacijaHotela = (idHotel) => api.get(REST_API_BASE_URL + `/admin/hotel/${idHotel}`);
