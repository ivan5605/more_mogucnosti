import { apiAuth } from "./Config";

const base = "/slikaHotel";

export const addSlikaH = (idHotel, slikaDto) => apiAuth.post(`${base}/admin/create/${idHotel}`, slikaDto);

export const deleteSlikaH = (idSlika) => apiAuth.delete(`${base}/admin/delete/${idSlika}`);

export const setGlavnaH = (idSlika) => apiAuth.put(`${base}/admin/setGlavna/${idSlika}`);