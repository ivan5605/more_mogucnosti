import React, { useState, useEffect } from 'react';
import { prijavljeni } from '../services/AuthService';
import { toast } from 'react-toastify';           // ✅ koristimo react-toastify
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';

const ProfilComponent = () => {
  const [email, setEmail] = useState("");         // ✅ string, ne array
  const navigate = useNavigate();                 // ✅ pozvati hook

  function setPrijavljeniKorisnik() {
    prijavljeni()
      .then(response => {
        const val = response.data?.email ?? response.data;
        setEmail(val);
        console.log("Email:", val);
      })
      .catch(greska => {
        const poruka =
          greska.response?.data?.message ||
          greska.response?.data?.error ||
          "Dogodila se greška";

        toast.error(poruka, { autoClose: 2000, position: 'bottom-left' });

        setTimeout(() => {
          navigate('/prijava');                   // ✅ sada postoji
        }, 2000);

        console.error(greska.response?.data || greska.message);
      });
  }

  useEffect(() => {
    setPrijavljeniKorisnik();
  }, []);                                         // ✅ pozovi jednom

  return (
    <div>
      <p className='mt-5 py-5'>{email}</p>
    </div>
  );
};

export default ProfilComponent;
