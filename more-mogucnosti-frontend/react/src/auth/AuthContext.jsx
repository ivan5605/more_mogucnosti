import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthCtx = createContext(null);
//kreiram kontekst, "globalna ladica" u koju možemo spremati vrijednosti i čitati ih bilo gdi

//omotač, sve kaj prikazemj unutar njega more čitati auth stanje
export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [loggedIn, setLoggedIn] = useState(false); //boolean koji posle koristim
  const timerRef = useRef(null); //cuva ID timera

  const startTimer = (expiresAt) => {
    clearTimeout(timerRef.current); //makni stari timer
    if (!expiresAt) return; //nema expAt nema timera

    const delay = Math.max(0, expiresAt - Date.now()); //kolko ms je ostalo
    timerRef.current = setTimeout(() => {
      logout(); // automatski logout na istek
    }, delay);
  };

  // učitaj stanje iz localStorage na mount
  useEffect(() => {
    const token = localStorage.getItem('token');
    const expStr = localStorage.getItem('expAt');
    const exp = expStr ? Number(expStr) : null;

    const valid = !!token && !!exp && Date.now() < exp;
    setLoggedIn(valid);
    if (valid) startTimer(exp); else {
      // ako je već isteklo, očisti ostatke
      localStorage.removeItem('token');
      localStorage.removeItem('expAt');
    }

    return () => clearTimeout(timerRef.current);
  }, []);

  const login = (token, expiresAt) => {
    localStorage.setItem('token', token);
    localStorage.setItem('expAt', String(expiresAt));
    setLoggedIn(true);
    startTimer(expiresAt); //krece odbrojavanje
  };

  const logout = () => {
    clearTimeout(timerRef.current);
    localStorage.removeItem('token');
    localStorage.removeItem('expAt');
    setLoggedIn(false);
    navigate('/prijava', { replace: true }); // preusmjeri na login
  };

  return (
    <AuthCtx.Provider value={{ loggedIn, login, logout }}>
      {children}
    </AuthCtx.Provider>
  ); //izlozi svoje vrijednosti (loggedIn, login, logout) svim komponentama ispod sebe u stablu
  //bilo koja komponenta unutar tog providera može pozvati useAtuh() i dobiti kontekst
}

export function useAuth() {
  return useContext(AuthCtx);
}
