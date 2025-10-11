import { apiAuth } from "./Config";

const base = "/korisnik";

export const getKorisniciWithCount = () => apiAuth.get(`${base}/admin/count`);

export const getKorisnik = (idKorisnik) => apiAuth.get(`${base}/${idKorisnik}`);

export const deleteKorisnik = (deleteDto) => apiAuth.delete(`${base}/delete`, { data: deleteDto });

export const updateKorisnik = (azuriraniKor) => apiAuth.put(`${base}/update`, azuriraniKor);

export const updateLozinka = (promjenaLozinkaDto) => apiAuth.put(`${base}/updateLozinka`, promjenaLozinkaDto);

export const adminDeleteKorisnik = (idKorisnik) => apiAuth.delete(`${base}/admin/delete/${idKorisnik}`);