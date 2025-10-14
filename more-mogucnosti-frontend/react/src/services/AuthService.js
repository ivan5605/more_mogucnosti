import { apiPublic, apiAuth } from "./Config";

const base = "/auth";

export const registracija = (registracijaRequest) => apiPublic.post(`${base}/register`, registracijaRequest);

export const prijava = (prijavaRequest) => apiPublic.post(`${base}/login`, prijavaRequest);

export const prijavljeni = () => apiAuth.get(`${base}/me`);
