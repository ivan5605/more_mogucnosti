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
          </Routes>
        </main>
        <FooterComponent />
      </BrowserRouter>

    </>
  )
}

export default App
