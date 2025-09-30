import React from 'react';
import logo from '../assets/logo.png';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useAuth } from '../auth/AuthContext';

const HeaderComponent = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { loggedIn, logout } = useAuth();
  const uloga = localStorage.getItem("uloga");

  const linkClass = (to) =>
    pathname === to ? 'nav-link px-2 link-secondary' : 'nav-link px-2 link-dark';
  //koja je aktivna

  const handleLogout = () => {
    logout();
    toast.success('Uspješno odjavljeni!', {
      autoClose: 1500,
      position: 'bottom-left'
    });
  };

  return (
    <div className="container-fluid">
      <header className="sticky-top top-0 start-0 w-100 bg-white border-bottom z-3">
        <div className='d-flex align-items-center py-3 px-3 position-relative'>

          <div className="d-flex align-items-center">
            <img src={logo} height={40} alt="Logo" />
            <h4 className="ms-2 mb-0">More Mogućnosti</h4>
          </div>

          <div className='position-absolute start-50 translate-middle-x'>
            <ul className="nav mb-2 mb-md-0">
              <li className="nav-item"><Link to="/" className={linkClass('/')}>Početna</Link></li>
              <li className="nav-item"><Link to="/nasiHoteli" className={linkClass('/nasiHoteli')}>Naši hoteli</Link></li>
              <li className="nav-item"><Link to="/kontakt" className={linkClass('/kontakt')}>Kontakt</Link></li>
              <li className="nav-item"><Link to="/onama" className={linkClass('/onama')}>O nama</Link></li>
            </ul>
          </div>


          {!loggedIn ? (
            <div className='ms-auto'>
              <button
                className="btn btn-outline-primary me-2"
                onClick={() => navigate('/prijava')}
              >
                Prijava
              </button>
              <button
                className="btn btn-primary"
                onClick={() => navigate('/registracija')}
              >
                Registracija
              </button>
            </div>
          ) : (
            uloga === 'ADMIN' ? (
              <>
                <div className='dropdown ms-auto'>
                  <button className='btn btn-outline-secondary dropdown-toggle' type='button' id='dropdownBtn' data-bs-toggle='dropdown' aria-expanded='false'>
                    <i className="bi bi-list"></i>
                  </button>
                  <ul className='dropdown-menu' aria-labelledby='dropdownBtn'>
                    <li><a className='dropdown-item' href="/admin">Korisnici</a></li>
                    <li><a href="/adminHoteli" className='dropdown-item'>Hoteli</a></li>
                    <li><a href="/profil" className='dropdown-item'>Profil</a></li>
                    <li><button className='dropdown-item' onClick={handleLogout}>Odjava</button></li>
                  </ul>
                </div>
              </>
            ) : (
              <>
                <div className='dropdown ms-auto'>
                  <button className='btn btn-outline-secondary dropdown-toggle' type='button' id='dropdownBtn' data-bs-toggle='dropdown' aria-expanded='false'>
                    <i class="bi bi-list"></i>
                  </button>
                  <ul className='dropdown-menu' aria-labelledby='dropdownBtn'>
                    <li><a className='dropdown-item' href="/profil">Profil</a></li>
                    <li><button className='dropdown-item' onClick={handleLogout}>Odjava</button></li>
                  </ul>
                </div>
              </>
            )
          )}

        </div>

      </header>
    </div>
  );
};

export default HeaderComponent;
