import { apiPublic, apiAuth } from "./Config";

const base = "/recenzija";

export const getRecenzijeHotela = (idHotel) => apiPublic.get(`${base}/hotel/${idHotel}`);

export const upsertRecenzija = (idHotel, recenzija) => apiAuth.put(`${base}/moja/hotel/${idHotel}`, recenzija);

export const getInfoRecenzija = (idHotel) => apiPublic.get(`${base}/hotel/info/${idHotel}`);

export const getRecenzijeKorisnika = () => apiAuth.get(`${base}/korisnik`);

export const getRecenzijeByIdKorisnik = (idKorisnik) => apiAuth.get(`${base}/admin/korisnik/${idKorisnik}`);

export const deleteRecenzija = (idRecenzija) => apiAuth.delete(`${base}/delete/${idRecenzija}`);