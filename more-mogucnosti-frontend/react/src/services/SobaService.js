import { apiPublic, apiAuth } from "./Config";

const base = "/soba";

export const getSoba = (idSoba) => apiPublic.get(`${base}/${idSoba}`);

export const getSobeHotela = (idHotel) => apiPublic.get(`${base}/hotel/${idHotel}`);

export const getSobaWithHotelAndSlike = (idSoba) => apiPublic.get(`${base}/withHotelAndSlike/${idSoba}`);

export const addSoba = (idHotel, sobaDto) => apiAuth.post(`${base}/admin/create/${idHotel}`, sobaDto);

export const updateSoba = (idSoba, sobaDto) => apiAuth.put(`${base}/admin/update/${idSoba}`, sobaDto);

export const softDeleteSoba = (idSoba) => apiAuth.put(`${base}/admin/softDelete/${idSoba}`);

export const aktivirajSoba = (idSoba) => apiAuth.put(`${base}/admin/aktiviraj/${idSoba}`);