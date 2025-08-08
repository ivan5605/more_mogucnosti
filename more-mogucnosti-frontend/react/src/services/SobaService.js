import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/soba';

export const getSoba = (idSoba) => axios.get(REST_API_BASE_URL + '/' + idSoba);

export const getSobeHotela = (idHotel) => axios.get(REST_API_BASE_URL + '/hotel/' + idHotel);

export const get2SobeHotela = (idHotel) => axios.get(REST_API_BASE_URL + '/hotel/random/' + idHotel);