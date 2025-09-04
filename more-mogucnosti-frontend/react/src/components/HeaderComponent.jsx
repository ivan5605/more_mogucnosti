import React from 'react';
import logo from '../assets/logo.png';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useAuth } from '../auth/AuthContext';

const HeaderComponent = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { loggedIn, logout } = useAuth();

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
      <header className="d-flex flex-wrap align-items-center justify-content-between py-3 border-bottom bg-white position-fixed top-0 start-0 w-100 z-3 px-3">
        <div className="d-flex align-items-center">
          <img src={logo} height={40} alt="Logo" />
          <h4 className="ms-2 mb-0">More Mogućnosti</h4>
        </div>

        <ul className="nav mb-2 mb-md-0">
          <li className="nav-item"><Link to="/" className={linkClass('/')}>Početna</Link></li>
          <li className="nav-item"><Link to="/nasiHoteli" className={linkClass('/nasiHoteli')}>Naši hoteli</Link></li>
          <li className="nav-item"><Link to="/kontakt" className={linkClass('/kontakt')}>Kontakt</Link></li>
          <li className="nav-item"><Link to="/onama" className={linkClass('/onama')}>O nama</Link></li>
        </ul>

        <div className="text-end">
          {!loggedIn ? (
            <>
              <button className="btn btn-outline-primary me-2" onClick={() => navigate('/prijava')}>Prijava</button>
              <button className="btn btn-primary" onClick={() => navigate('/registracija')}>Registracija</button>
            </>
          ) : (
            <>
              <button className="btn btn-outline-primary me-2" onClick={() => navigate('/profil')}>Profil</button>
              <button className="btn btn-primary" onClick={handleLogout}>Odjava</button>
            </>
          )}
        </div>
      </header>
    </div>
  );
};

export default HeaderComponent;
