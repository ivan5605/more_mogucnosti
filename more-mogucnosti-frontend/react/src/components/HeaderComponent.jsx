import React from 'react';
import logo from '../assets/logo.png'; // Ispravna putanja
import { useLocation, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify';

const HeaderComponent = () => {

  const navigator = useNavigate();

  const stranica = useLocation();
  const trenutnaPutanja = stranica.pathname;

  const postaviTrenutnuStranicu = (putanja) => trenutnaPutanja === putanja ? 'nav-link px-2 link-secondary' : 'nav-link px-2 link-dark';

  const handleProfilClick = (e) => {
    e.preventDefault(); // spriječi normalni <a> redirect
    const token = localStorage.getItem("token");

    if (!token) {
      toast.error("Morate biti prijavljeni!", {
        autoClose: 2000,
        position: "bottom-left",
      });
      return; // ne navigira
    }

    // ako ima token -> idi na profil
    navigator("/profil");
  };

  return (
    <div className='container-fluid'>
      <header className='d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 border-bottom bg-white position-fixed top-0 start-0 w-100 z-3 px-3'>
        <div className='d-flex col-md-3 mb-2 mb-md-0'>
          <img src={logo} height={40} alt="" />
          <h4 className='ms-2 '>More Mogućnosti</h4>
        </div>
        <ul className='nav col-12 col-md-auto mb-2 justify-content-center'>
          <li>
            <a href="/" className={postaviTrenutnuStranicu('/')}>Početna</a>
          </li>
          <li>
            <a href="/nasiHoteli" className={postaviTrenutnuStranicu('/nasiHoteli')}>Naši hoteli</a>
          </li>
          <li>
            <a href="/kontakt" className={postaviTrenutnuStranicu('/kontakt')}>Kontakt</a>
          </li>
          <li>
            <a href="/onama" className={postaviTrenutnuStranicu('/onama')}>O nama</a>
          </li>
          <li>
            <a href="/profil" className={postaviTrenutnuStranicu('/profil')} onClick={handleProfilClick}>Profil</a>
          </li>
        </ul>
        <div className='col-md-3 text-end'>
          <button type='button' className='btn btn-outline-primary me-2' onClick={() => navigator('/prijava')}>Prijava</button>
          <button type='button' className='btn btn-primary' onClick={() => navigator('/registracija')}>Registracija</button>
        </div>
      </header>
    </div>
  )
}

export default HeaderComponent;
