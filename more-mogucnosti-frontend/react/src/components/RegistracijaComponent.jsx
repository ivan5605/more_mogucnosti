import React, { useState } from 'react'
import { addKorisnik } from '../services/KorisnikService'
import { useNavigate } from 'react-router-dom'
import { ToastContainer, toast } from 'react-toastify';
import { registracija } from '../services/AuthService';


const RegistracijaComponent = () => {

  const navigator = useNavigate();

  const [korisnik, setKorisnik] = useState({
    ime: '',
    prezime: '',
    email: '',
    lozinka: '',
    lozinkaPotvrda: ''
  })

  const [errors, setErrors] = useState({
    ime: '',
    prezime: '',
    email: '',
    lozinka: '',
    lozinkaPotvrda: ''
  })

  function provjeriIme(ime) {
    if (!ime.trim()) {
      return "Unesite ime!";
    } else {
      return "";
    }
  }

  function provjeriPrezime(prezime) {
    if (!prezime.trim()) {
      return "Unesite prezime!";
    } else {
      return "";
    }
  }

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
    } else if (lozinka.length < 8) {
      return "Lozinka mora imati najmanje 8 znakova!"
    } else if (!/[A-Z]/.test(lozinka)) {
      return "Lozinka mora imati barem jedno veliko slovo!"
    } else if (!/\d/.test(lozinka)) {
      return "Lozinka mora imati barem jedan broj!"
    }
    return "";
  }

  function provjeriLozinkaPotvrda(lozinka, lozinkaPotvrda) {
    if (lozinka === lozinkaPotvrda) {
      return "";
    } else {
      return "Lozinke se ne podudaraju!";
    }
  }

  function provjeriUnos() {

    // const errorsCopy = { ...errors }

    // errorsCopy.ime = provjeriIme(korisnik.ime);
    // errorsCopy.prezime = provjeriPrezime(korisnik.prezime);
    // errorsCopy.email = provjeriEmail(korisnik.email);
    // errorsCopy.lozinka = provjeriLozinka(korisnik.lozinka);
    // errorsCopy.lozinkaPotvrda = provjeriLozinkaPotvrda(korisnik.lozinka, korisnik.lozinkaPotvrda);

    const errorsCopy = {
      ime: provjeriIme(korisnik.ime),
      prezime: provjeriPrezime(korisnik.prezime),
      email: provjeriEmail(korisnik.email),
      lozinka: provjeriLozinka(korisnik.lozinka),
      lozinkaPotvrda: provjeriLozinkaPotvrda(korisnik.lozinka, korisnik.lozinkaPotvrda)
    };

    setErrors(errorsCopy);

    // Vraća true ako nema nijedne greške
    return Object.values(errorsCopy).every(error => error === "");
  }

  function registrajKorisnik(dogadaj) {
    dogadaj.preventDefault();

    if (provjeriUnos()) {
      console.log("Korisnik podaci: ", korisnik);

      registracija(korisnik).then(response => {
        console.log("Korisnik uspješno registriran!", response.data);
        toast.success('Registracija uspješna!', {
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

          if (status === 409) {
            //409 - Conflict - vec postoji email adresa
            setErrors(prev => ({ ...prev, email: poruka }))
          } else {
            console.error("Greška kod registracije!", poruka);
          }
        }

      })
    }
  }


  return (
    <section className='py-5 mt-5' style={{ backgroundColor: '#eee' }}>
      <div className='container h-100 mb-5'>
        <div className='row d-flex justify-content-center align-items-center h-100'>
          <div className='col-lg-12 col-xl-11'>
            <div className='card shadow text-black' style={{ borderRadius: '25px' }}>
              <div className='card-body p-md-3'>
                <div className='row justify-content-center'>
                  <div className='col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1'>
                    <p className='text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4'>Registriraj se!</p>

                    <form className='mx-1 mx-md-4'>

                      <div className='d-flex flex-row align-items-center mb-4'>
                        <i className='fas fa-user fa-lg me-3 fa-fw'></i>
                        <div className={`flex-fill mb-0`}>
                          <input
                            type="text"
                            className={`form-control ${errors.ime ? 'is-invalid' : ''}`}
                            placeholder='Unesite ime:'
                            name='ime'
                            value={korisnik.ime}
                            onChange={(dogadaj) => setKorisnik({ ...korisnik, ime: dogadaj.target.value })}
                          />
                          {errors.ime && <div className='invalid-feedback'>{errors.ime}</div>}
                        </div>
                      </div>

                      <div className='d-flex flex-row align-items-center mb-4'>
                        <i className='fas fa-address-card fa-lg me-3 fa-fw'></i>
                        <div className={`flex-fill mb-0`}>
                          <input
                            type="text"
                            className={`form-control ${errors.prezime ? 'is-invalid' : ''}`}
                            placeholder='Unesite prezime:'
                            name='prezime'
                            value={korisnik.prezime}
                            onChange={(dogadaj) => setKorisnik({ ...korisnik, prezime: dogadaj.target.value })}
                          />
                          {errors.prezime && <div className='invalid-feedback'>{errors.prezime}</div>}
                        </div>
                      </div>

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

                      <div className='d-flex flex-row align-items-center mb-4'>
                        <i className='fas fa-key fa-lg me-3 fa-fw'></i>
                        <div className={`flex-fill mb-0`}>
                          <input
                            type="password"
                            className={`form-control ${errors.lozinkaPotvrda ? 'is-invalid' : ''}`}
                            placeholder='Ponovno unesite lozinku:'
                            name='lozinkaPotvrda'
                            value={korisnik.lozinkaPotvrda}
                            onChange={(dogadaj) => setKorisnik({ ...korisnik, lozinkaPotvrda: dogadaj.target.value })}
                          />
                          {errors.lozinkaPotvrda && <div className='invalid-feedback'>{errors.lozinkaPotvrda}</div>}
                        </div>
                      </div>

                      <div className='form-check d-flex justify-content-center mb-5'>
                        <input type="checkbox" className='form-check-input me-2' value={""} id='check' />
                        <label className='form-check-label' htmlFor='check'>
                          Pristajem na sve <a href="#">Terms of service</a>
                        </label>
                      </div>

                      <div className='d-flex justify-content-center mx-4 mb-3 mb-lg-4'>
                        <button type='button' className='btn btn-primary btn-lg' onClick={(dogadaj) => registrajKorisnik(dogadaj)}>Registracija</button>
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

export default RegistracijaComponent