// src/AppRouter.jsx
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Layout
import HeaderComponent from "../components/HeaderComponent";
import FooterComponent from "../components/FooterComponent";

// PAGES (preimenovano na *Page)
import PocetnaPage from "../pages/PocetnaPage";
import RegistracijaPage from "../pages/RegistracijaPage";
import NasiHoteliPage from "../pages/NasiHoteliPage";
import HotelPage from "../pages/HotelPage";
import RezervacijaPage from "../pages/RezervacijaPage";
import LoginPage from "../pages/LoginPage";
import ProfilPage from "../pages/ProfilPage";
import KorisnikDetaljiPage from "../pages/KorisnikDetaljiPage";
import AdminPage from "../pages/AdminPage";
import UrediHotelPage from "../pages/UrediHotelPage";
import UrediSobaPage from "../pages/UrediSobaPage";
import { AuthProvider } from "../auth/AuthContext";

const NotFound = () => (
  <div className="container py-5 mt-5">
    <h1 className="display-6">404</h1>
    <p>Stranica nije pronađena.</p>
  </div>
);

export default function AppRouter() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <HeaderComponent />
        <main>
          <Routes>
            {/* http://localhost:3000 */}
            <Route path="/" element={<PocetnaPage />} />

            {/* http://localhost:3000/registracija */}
            <Route path="/registracija" element={<RegistracijaPage />} />

            {/* http://localhost:3000/nasiHoteli */}
            <Route path="/nasiHoteli" element={<NasiHoteliPage />} />

            {/* http://localhost:3000/hotel/:idHotel */}
            <Route path="/hotel/:idHotel" element={<HotelPage />} />

            {/* http://localhost:3000/rezervacija/:idSoba */}
            <Route path="/rezervacija/:idSoba" element={<RezervacijaPage />} />

            {/* http://localhost:3000/prijava */}
            <Route path="/prijava" element={<LoginPage />} />

            {/* http://localhost:3000/profil */}
            <Route path="/profil" element={<ProfilPage />} />

            {/* http://localhost:3000/admin */}
            <Route path="/admin" element={<AdminPage />} />

            {/* http://localhost:3000/korisnik/detalji/:idKorisnik */}
            <Route path="/korisnik/detalji/:idKorisnik" element={<KorisnikDetaljiPage />} />

            {/* http://localhost:3000/admin/urediHotel/:idHotel */}
            <Route path="/admin/urediHotel/:idHotel" element={<UrediHotelPage />} />

            {/* http://localhost:3000/admin/urediSoba/:idSoba (ako koristiš) */}
            <Route path="/admin/urediSoba/:idSoba" element={<UrediSobaPage />} />

            {/* 404 */}
            <Route path="*" element={<NotFound />} />
          </Routes>
        </main>
        <FooterComponent />
      </AuthProvider>
    </BrowserRouter>
  );
}
