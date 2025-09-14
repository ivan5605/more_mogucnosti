import { useState } from 'react'
import './App.css'
import HeaderComponent from './components/HeaderComponent'
import FooterComponent from './components/FooterComponent'
import LoginComponent from './components/LoginComponent'
import 'bootstrap-icons/font/bootstrap-icons.css';
import PocetnaComponent from './components/PocetnaComponent'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import RegistracijaComponent from './components/RegistracijaComponent'
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify'
import NasiHoteliComponent from './components/NasiHoteliComponent'
import HotelComponent from './components/HotelComponent'
import RezervacijaComponent from './components/RezervacijaComponent'
import ProfilComponent from './components/ProfilComponent'
import 'react-datepicker/dist/react-datepicker.css';
import KorisnikDetaljiComponent from './components/KorisnikDetaljiComponent';
import { AuthProvider } from './auth/AuthContext';
import RecenzijaComponent from './components/RecenzijaComponent';
import AdminComponent from './components/AdminComponent';


function App() {

  return (
    <>
      <BrowserRouter>
        <AuthProvider>
          <ToastContainer />
          <HeaderComponent />
          <main>
            <Routes>
              {/* http://localhost:3000 */}
              <Route path='/' element={<PocetnaComponent />}></Route>

              {/* http://localhost:3000/registracija */}
              <Route path='/registracija' element={<RegistracijaComponent />}></Route>

              {/* http://localhost:3000/nasiHoteli */}
              <Route path='/nasiHoteli' element={<NasiHoteliComponent />}></Route>

              {/* http://localhost:3000/hotel/idHotel */}
              <Route path='/hotel/:idHotel' element={<HotelComponent />}></Route>

              {/* http://localhost:3000/rezervacija/idSoba */}
              <Route path='/rezervacija/:idSoba' element={<RezervacijaComponent />}></Route>

              {/* http://localhost:3000/prijava */}
              <Route path='/prijava' element={<LoginComponent />}></Route>

              {/* http://localhost:3000/profil */}
              <Route path='/profil' element={<ProfilComponent />}></Route>

              {/* http://localhost:3000/recenzija/idHotel */}
              <Route path='/recenzija/:idHotel' element={<RecenzijaComponent />}></Route>

              {/* http://localhost:3000/admin */}
              <Route path='/admin' element={<AdminComponent />}></Route>

              {/* http://localhost:3000/korisnik/detalji/idKorisnik */}
              <Route path='/korisnik/detalji/:idKorisnik' element={<KorisnikDetaljiComponent />}></Route>
            </Routes>
          </main>
          <FooterComponent />
        </AuthProvider>
      </BrowserRouter>

    </>
  )
}

export default App
