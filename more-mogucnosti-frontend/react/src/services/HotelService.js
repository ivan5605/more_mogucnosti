import axios from 'axios';

const REST_API_BASE_URL = 'http://localhost:8080/api/hotel'

export const getAllHoteli = () => axios.get(REST_API_BASE_URL)

export const getRandomHoteli = () => axios.get(REST_API_BASE_URL + "/random")