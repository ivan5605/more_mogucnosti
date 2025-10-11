import { apiPublic, apiAuth } from "./Config";

const base = "/hotel";

export const getAllHoteli = () => apiPublic.get(base);

export const getRandomHoteli = () => apiPublic.get(`${base}/random`);

export const getHotel = (idHotel) => apiPublic.get(`${base}/${idHotel}`);

export const updateHotel = (idHotel, hotelDto) => apiAuth.put(`${base}/admin/update/${idHotel}`, hotelDto);

export const softDeleteHotel = (idHotel) => apiAuth.put(`${base}/admin/softDelete/${idHotel}`);

export const createHotel = (hotelDto) => apiAuth.post(`${base}/admin/create`, hotelDto);

export const aktivirajHotel = (idHotel) => apiAuth.put(`${base}/admin/aktiviraj/${idHotel}`);