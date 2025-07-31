import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/korisnik';

export const addKorisnik = (korisnik) => axios.post(REST_API_BASE_URL, korisnik);