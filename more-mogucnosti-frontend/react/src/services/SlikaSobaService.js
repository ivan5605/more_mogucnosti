import { apiAuth } from "./Config";

const base = "/slikaSoba";

export const addSlikaS = (idSoba, slikaDto) => apiAuth.post(`${base}/admin/create/${idSoba}`, slikaDto);

export const deleteSlikaS = (idSlika) => apiAuth.delete(`${base}/admin/delete/${idSlika}`);

export const setGlavnaS = (idSlika) => apiAuth.put(`${base}/admin/setGlavna/${idSlika}`);