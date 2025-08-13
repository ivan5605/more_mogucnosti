import axios from "axios";

const REST_API_BASE_URL = "http://localhost:8080/api/rezervacija"

export const createRezervacija = (rezervacija) => axios.post(REST_API_BASE_URL, rezervacija); 