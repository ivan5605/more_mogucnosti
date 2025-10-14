import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthCtx = createContext(null);
//kontekst, globalna ladica u koju morem spremati vrijednosti i citati ih bilo gdi

//omotač, sve kaj prikazemj unutar njega more čitati auth stanje
export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [loggedIn, setLoggedIn] = useState(false); //boolean koji posle koristim
  const timerRef = useRef(null);

  const startTimer = (expAt) => {
    clearTimeout(timerRef.current); //makni stari timer
    if (!expAt) return; //nema expAt nema timera

    const delay = Math.max(0, expAt - Date.now()); //kolko ms je ostalo
    timerRef.current = setTimeout(() => {
      logout(); //auto odjava na istek
    }, delay);
  };

  //ucitaj stanje iz localStorage
  useEffect(() => {
    const token = localStorage.getItem('token');
    const expStr = localStorage.getItem('expAt');
    const exp = expStr ? Number(expStr) : null;

    const valid = !!token && !!exp && Date.now() < exp;
    setLoggedIn(valid);
    if (valid) startTimer(exp); else {
      localStorage.removeItem('token');
      localStorage.removeItem('expAt');
    }

    return () => clearTimeout(timerRef.current);
  }, []);

  const login = (token, expAt) => {
    localStorage.setItem('token', token);
    localStorage.setItem('expAt', String(expAt));
    setLoggedIn(true);
    startTimer(expAt); //krece odbrojavanje
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
  ); //izlozi svoje vrijednosti (loggedIn, login, logout) svim komponentama ispod sebe
  //bilo koja komponenta unutar tog providera mozi - useAtuh() i dobiti kontekst
}

export function useAuth() {
  return useContext(AuthCtx);
}
