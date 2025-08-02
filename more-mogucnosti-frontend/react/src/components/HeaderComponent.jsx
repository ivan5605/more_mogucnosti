import React from 'react';
import logo from '../assets/logo.png'; // Ispravna putanja
import { useNavigate } from 'react-router-dom'

const HeaderComponent = () => {

  const navigator = useNavigate();

  return (
    <div className='container-fluid'>
      <header className='d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-2 border-bottom'>
        <div className='d-flex col-md-3 mb-2 mb-md-0'>
          <img src={logo} height={40} alt="" />
          <h4 className='ms-2 '>More Mogućnosti</h4>
        </div>
        <ul className='nav col-12 col-md-auto mb-2 justify-content-center'>
          <li>
            <a href="/" className='nav-link px-2 link-secondary'>Početna</a>
          </li>
          <li>
            <a href="/nasiHoteli" className='nav-link px-2 link-dark'>Naši hoteli</a>
          </li>
          <li>
            <a href="/kontakt" className='nav-link px-2 link-dark'>Kontakt</a>
          </li>
          <li>
            <a href="/onama" className='nav-link px-2 link-dark'>O nama</a>
          </li>
        </ul>
        <div className='col-md-3 text-end'>
          <button type='button' className='btn btn-outline-primary me-2'>Prijava</button>
          <button type='button' className='btn btn-primary' onClick={() => navigator('/registracija')}>Registracija</button>
        </div>
      </header>
    </div>
  )
}

export default HeaderComponent;
