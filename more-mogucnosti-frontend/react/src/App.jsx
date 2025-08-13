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

function App() {
  return (
    <>
      <BrowserRouter>
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
          </Routes>
        </main>
        <FooterComponent />
      </BrowserRouter>

    </>
  )
}

export default App
