import React, { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getSobaWithHotelAndSlike } from '../services/SobaService'
import { createRezervacija } from '../services/RezervacijaService'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { toast } from 'react-toastify';

const RezervacijaComponent = () => {

  const { idSoba } = useParams();
  const [soba, setSoba] = useState(null);
  //bolje jer je kao početna vrijednost null kaj označava da podatka još nema
  //jasno signaliziram da podatak nije učitan i u JSX-u to onda morem i provjeriti

  //ako stavim useState([]) - početna vrijednost je prazno polje
  //frontendu to veli da soba već postoji i da je tip array - kaj možda nije točno jer je soba iz backenda zapravo objekt

  const navigator = useNavigate();

  const [rezervacija, setRezervacija] = useState({
    sobaId: idSoba,
    brojOsoba: 1,
    datumPocetak: '',
    datumKraj: ''
  })

  const [errors, setErrors] = useState({
    datumPocetak: '',
    datumKraj: ''
  })

  const [zauzeto, setZauzeto] = useState({
    greska: ''
  })

  function provjeriDatumDolazak(datumPocetak) {
    if (datumPocetak) {
      const danas = new Date();
      danas.setHours(0, 0, 0, 0);
      const dolazak = new Date(datumPocetak);
      if (dolazak < danas) {
        return "Datum dolazka ne može biti prije današnjeg datuma!"
      } else {
        return "";
      }
    } else {
      return "Unesite datum dolaska!"
    }
  }

  function provjeriDatumOdlazak(datumPocetak, datumKraj) {
    if (datumKraj) {
      const dolazak = new Date(datumPocetak);
      const odlazak = new Date(datumKraj);
      if (odlazak < dolazak) {
        return "Datum odlazka mora biti na isti dan ili nakon datuma dolaska!"
      } else {
        return "";
      }
    } else {
      return "Unesite datum odlaska!"
    }
  }

  function provjeriUnos() {
    const errorsCopy = {
      datumPocetak: provjeriDatumDolazak(rezervacija.datumPocetak),
      datumKraj: provjeriDatumOdlazak(rezervacija.datumPocetak, rezervacija.datumKraj)
    };

    setErrors(errorsCopy);

    return Object.values(errorsCopy).every(error => error === "");
  }

  function rezerviraj(dogadaj) {
    dogadaj.preventDefault();
    const provjera = provjeriUnos();

    if (provjera) {
      console.log("Rezervacija podaci: ", rezervacija);

      createRezervacija(rezervacija).then(response => {
        console.log("Rezervacija izrađena!", response.data);
        toast.success('Rezervacija izrađena!', {
          autoClose: 2000,
          position: 'bottom-left'
        })
        setTimeout(() => {
          navigator('/')
        }, 2000)
      }).catch(error => {
        if (error.response) {
          const status = error.response.status;
          const poruka = error.response.data?.message || "Greška na serveru."
          //falsy - ako lijeva strana bude undefined, null, "" - onda bude ispisal to

          if (status === 409) {
            //409 - conflict - vec postoji rezervacija u tom rasponu datuma
            setZauzeto(error => ({ ...error, greska: poruka }))
          } else if (status === 403) {
            console.log("Korisnik nije prijavljen!", poruka);
            toast.error('Za izradu rezervacije morate biti prijavljeni!', {
              autoClose: 5000,
              position: 'bottom-left'
            })
          } else {
            console.error("Greška kod izrade rezervacije!", poruka)
          }
        }
      })
    }
  }

  useEffect(() => {
    getSobaWithHotelAndSlike(idSoba).then((response) => {
      setSoba(response.data);
    }).catch((error) => {
      console.error('Greška kod dohvaćanja sobe hotela!', error)
    })
  }, [idSoba]);

  if (!soba) {
    return <div className='container mt-5'>Učitavanje...</div>
  }

  const slikeSobe = [
    soba.glavnaSlika,
    ...(Array.isArray(soba.sporedneSlike) ? soba.sporedneSlike : []),
  ];

  return (
    <div className="container py-5 mt-5">
      <h2 className="text-center mb-4 fw-bold display-5">
        Rezervacija - {soba.hotel.naziv}
      </h2>

      <div className="text-center mb-4 text-muted">
        {soba.hotel.grad.imeGrad}, {soba.hotel.adresa}
      </div>

      {/* Glavni layout */}
      <div className="row g-4  align-items-stretch">
        {/* Lijeva strana - slike i detalji */}
        <div className="col-lg-7 d-flex flex-column">
          {/* Carousel */}
          <div
            id="sobaCarousel"
            className="carousel slide mb-4"
            data-bs-ride="carousel"
          >
            <div className="carousel-inner">
              {slikeSobe.map((slika, index) => (
                <div
                  className={`carousel-item ${index === 0 ? 'active' : ''}`}
                  key={index}
                >
                  <img
                    src={slika.putanja}
                    alt={`Slika ${index + 1}`}
                    className="d-block w-100"
                    style={{
                      height: '400px',
                      objectFit: 'cover',
                      borderRadius: '10px',
                    }}
                  />
                </div>
              ))}
            </div>
            <button
              className="carousel-control-prev"
              type="button"
              data-bs-target="#sobaCarousel"
              data-bs-slide="prev"
            >
              <span className="carousel-control-prev-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Previous</span>
            </button>
            <button
              className="carousel-control-next"
              type="button"
              data-bs-target="#sobaCarousel"
              data-bs-slide="next"
            >
              <span className="carousel-control-next-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Next</span>
            </button>
          </div>

          {/* Detalji sobe */}
          <div className="card shadow-lg border-0 h-100">
            <div className="card-body">
              <h4 className="fw-bold mb-3">Detalji sobe</h4>
              <ul className="list-unstyled mb-0">
                <li><strong>Kapacitet:</strong> {soba.kapacitet} osoba/e</li>
                <li><strong>Cijena noćenja:</strong> {soba.cijenaNocenja} €</li>
                <li>
                  <strong>Balkon:</strong>{' '}
                  {soba.balkon ? (
                    <i className="fas fa-check text-success ms-1"></i>
                  ) : (
                    <i className="fas fa-times text-danger ms-1"></i>
                  )}
                </li>
                <li>
                  <strong>Pet friendly:</strong>{' '}
                  {soba.petFriendly ? (
                    <i className="fas fa-check text-success ms-1"></i>
                  ) : (
                    <i className="fas fa-times text-danger ms-1"></i>
                  )}
                </li>
              </ul>
            </div>
          </div>
        </div>

        {/* Desna strana - forma */}
        <div className="col-lg-5 d-flex">
          <div className="card shadow-lg border-0 p-5 w-100 h-100">
            <h4 className="fw-bold mb-5">Podaci o rezervaciji</h4>
            <form>
              {/* broj sobe */}
              <div className='mb-3'>
                <label className='form-label'>Broj sobe</label>
                <input
                  type="text"
                  className='form-control'
                  value={soba.brojSobe}
                  readOnly
                />
              </div>

              {/* broj osoba */}
              <div className="mb-3">
                <label className="form-label">Broj osoba</label>
                <input
                  type="number"
                  className="form-control"
                  min={1}
                  max={soba.kapacitet}
                  value={rezervacija.brojOsoba}
                  onChange={(e) =>
                    setRezervacija({ ...rezervacija, brojOsoba: e.target.value })
                  }
                />
                <small className="text-muted">
                  Maksimalno {soba.kapacitet} osoba/e
                </small>
              </div>

              {/* datum početak */}
              <div className="mb-3">
                <label className="form-label">Datum dolaska</label>
                <input
                  type="date"
                  className={`form-control ${errors.datumPocetak ? 'is-invalid' : ''}`}
                  value={rezervacija.datumPocetak}
                  onChange={(e) =>
                    setRezervacija({ ...rezervacija, datumPocetak: e.target.value })
                  }
                />
                {errors.datumPocetak && <div className='invalid-feedback'>{errors.datumPocetak}</div>}
              </div>

              {/* datum kraj */}
              <div className="mb-3">
                <label className="form-label">Datum odlaska</label>
                <input
                  type="date"
                  className={`form-control ${errors.datumKraj ? 'is-invalid' : ''}`}
                  value={rezervacija.datumKraj}
                  onChange={(e) =>
                    setRezervacija({ ...rezervacija, datumKraj: e.target.value })
                  }
                />
                {errors.datumKraj && <div className='invalid-feedback'>{errors.datumKraj}</div>}

              </div>
              {zauzeto.greska && <div className='alert alert-danger' role='alert'>{zauzeto.greska}</div>}


              <div className="d-flex mt-5">
                <button
                  type='button'
                  className="btn btn-primary btn-lg w-100 me-3"
                  data-bs-toggle='modal'
                  data-bs-target='#exampleModal'
                  onClick={(e) => rezerviraj(e)}
                >
                  Rezerviraj
                </button>
                <button className="btn btn-outline-danger btn-lg w-100">
                  Odustani
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RezervacijaComponent