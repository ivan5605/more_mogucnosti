import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { prijava } from '../services/AuthService';
import { toast } from 'react-toastify';
import { useAuth } from '../auth/AuthContext';

const LoginComponent = () => {

  const { login } = useAuth();

  const navigator = useNavigate();

  const [korisnik, setKorisnik] = useState({
    email: '',
    lozinka: ''
  })

  const [errors, setErrors] = useState({
    email: '',
    lozinka: '',
    podaci: ''
  })

  function provjeriEmail(email) {
    if (!email.trim()) {
      return "Unesite email!";
    } else {
      return "";
    }
  }

  function provjeriLozinka(lozinka) {
    if (!lozinka.trim()) {
      return "Unesite lozinku!"
    }
    return "";
  }

  function provjeriUnos() {

    const errorsCopy = {
      email: provjeriEmail(korisnik.email),
      lozinka: provjeriLozinka(korisnik.lozinka)
    };

    setErrors(errorsCopy);

    // Vraća true ako nema nijedne greške
    return Object.values(errorsCopy).every(error => error === "");
  }

  function prijavaKorisnik(dogadaj) {
    dogadaj.preventDefault();

    if (provjeriUnos()) {
      prijava(korisnik).then(response => {

        const token = response.data.token;
        localStorage.setItem("token", token);
        let expAt = response.data.expAt;
        localStorage.setItem("expAt", expAt);
        let uloga = response.data.uloga;
        localStorage.setItem("uloga", uloga);

        login(token, expAt);

        toast.success("Prijava uspješna!", {
          autoClose: 2000,
          position: 'bottom-left'
        })
        setTimeout(() => {
          navigator('/')
        }, 2000);
      }).catch(error => {
        if (error.response) {
          const status = error.response.status;
          const poruka = error.response.data?.message || "Greška na serveru."

          if (status === 401) {
            //401 - Unauthorized - pogrešni podaci za prijavu
            setErrors(prev => ({ ...prev, podaci: poruka }))
          } else {
            console.error("Greška kod prijave!", poruka);
          }
        } else {
          setErrors(prev => ({ ...prev, podaci: "Greška na serveru. Molimo pokušajte ponovno kasnije." }))
        }

      })



    }
  }

  return (
    <section className='h-100' style={{ backgroundColor: '#eee' }}>
      <div className='container h-100 mb-5'>
        <div className='row d-flex justify-content-center align-items-center h-100'>
          <div className='col-lg-12 col-xl-11'>
            <div className='card shadow text-black' style={{ borderRadius: '25px' }}>
              <div className='card-body p-md-3'>
                <div className='row justify-content-center'>
                  <div className='col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1'>
                    <p className='text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4'>Prijavite se!</p>

                    <form className='mx-1 mx-md-4'>

                      <div className='d-flex flex-row align-items-center mb-4'>
                        <i className='fas fa-envelope fa-lg me-3 fa-fw'></i>
                        <div className={`flex-fill mb-0`}>
                          <input
                            type="text"
                            className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                            placeholder='Unesite email:'
                            name='email'
                            value={korisnik.email}
                            onChange={(dogadaj) => setKorisnik({ ...korisnik, email: dogadaj.target.value })}
                          />
                          {errors.email && <div className='invalid-feedback'>{errors.email}</div>}
                        </div>
                      </div>

                      <div className='d-flex flex-row align-items-center mb-4'>
                        <i className='fas fa-lock fa-lg me-3 fa-fw'></i>
                        <div className={`flex-fill mb-0`}>
                          <input
                            type="password"
                            className={`form-control ${errors.lozinka ? 'is-invalid' : ''}`}
                            placeholder='Unesite lozinku:'
                            name='lozinka'
                            value={korisnik.lozinka}
                            onChange={(dogadaj) => setKorisnik({ ...korisnik, lozinka: dogadaj.target.value })}
                          />
                          {errors.lozinka && <div className='invalid-feedback'>{errors.lozinka}</div>}
                        </div>
                      </div>
                      {errors.podaci && <div className='alert alert-danger'>{errors.podaci}</div>}
                      <div className='d-flex justify-content-center mx-4 mb-3 mb-lg-4'>
                        <button type='button' className='btn btn-primary btn-lg' onClick={(dogadaj) => prijavaKorisnik(dogadaj)}>Prijava</button>
                      </div>

                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

export default LoginComponent