import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getSobaWithHotelAndSlike } from '../services/SobaService';
import { createRezervacija, getZauzetiDatumi } from '../services/RezervacijaService';
import { toast } from 'react-toastify';
import { prijavljeni } from '../services/AuthService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const RezervacijaComponent = () => {
  const { idSoba } = useParams();
  const [soba, setSoba] = useState(null);
  const navigator = useNavigate();

  const [rezervacija, setRezervacija] = useState({
    sobaId: Number(idSoba),
    brojOsoba: 1,
    datumPocetak: '',
    datumKraj: ''
  });

  const [errors, setErrors] = useState({
    datumPocetak: '',
    datumKraj: '',
    brojOsoba: ''
  });

  const [zauzeto, setZauzeto] = useState({ greska: '' });

  const [korisnik, setKorisnik] = useState({
    ime: '',
    prezime: '',
    email: ''
  });

  const [zauzetiTermini, setZauzetiTermini] = useState([]);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  const openModalBtnRef = useRef(null);
  const closeModalBtnRef = useRef(null);

  const toLocalDate = (yyyyMmDd) => (yyyyMmDd ? new Date(`${yyyyMmDd}T00:00:00`) : null);
  const toYMD = (d) => (d instanceof Date && !isNaN(d)) ? d.toLocaleDateString('sv-SE') : '';
  const addDays = (date, days) => {
    if (!date) return null;
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    d.setDate(d.getDate() + days);
    return d;
  };

  function cijenaRezervacije(datumPocetak, datumKraj, cijenaNocenja) {
    if (!datumPocetak || !datumKraj || !cijenaNocenja) return 0;
    const pocetak = new Date(datumPocetak);
    const kraj = new Date(datumKraj);
    const razlikaMs = kraj.getTime() - pocetak.getTime();
    const razlikaDan = razlikaMs / (1000 * 60 * 60 * 24);
    return razlikaDan * cijenaNocenja;
  }

  function setPrijavljeniKorisnik() {
    prijavljeni()
      .then(response => {
        setKorisnik({
          ime: response.data.ime,
          prezime: response.data.prezime,
          email: response.data.email
        });
      })
      .catch(console.error);
  }

  const postavljeniKorisnik = useRef(false);
  const [ucitavanje, setUcitavanje] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        if (!postavljeniKorisnik.current) {
          setPrijavljeniKorisnik();
          postavljeniKorisnik.current = true;
        }
        const [zauzetiRes, sobaRes] = await Promise.all([
          getZauzetiDatumi(idSoba),
          getSobaWithHotelAndSlike(idSoba),
        ]);
        setZauzetiTermini(Array.isArray(zauzetiRes.data) ? zauzetiRes.data : []);
        setSoba(sobaRes.data);
      } catch (err) {
        console.error('Greška kod dohvaćanja korisnika/sobe/termina!', err);
      } finally {
        setUcitavanje(false);
      }
    })();
  }, [idSoba]);

  if (ucitavanje) {
    return <div className='container py-5 mt-5'>Učitavanje...</div>;
  }

  const onChangePocetakDP = (date) => {
    setStartDate(date);
    setRezervacija(prev => ({ ...prev, datumPocetak: toYMD(date) }));
    const minEnd = addDays(date, 1);
    if (endDate && minEnd && endDate < minEnd) {
      setEndDate(null);
      setRezervacija(prev => ({ ...prev, datumKraj: '' }));
    }
    setErrors(prev => ({ ...prev, datumPocetak: '' }));
  };

  const onChangeKrajDP = (date) => {
    setEndDate(date);
    setRezervacija(prev => ({ ...prev, datumKraj: toYMD(date) }));
    setErrors(prev => ({ ...prev, datumKraj: '' }));
  };

  function provjeriDatumDolazak(datumPocetak) {
    if (datumPocetak) {
      const danas = new Date();
      danas.setHours(0, 0, 0, 0);
      const dolazak = new Date(datumPocetak);
      if (dolazak < danas) return 'Datum dolazka ne može biti prije današnjeg datuma!';
      return '';
    }
    return 'Unesite datum dolaska!';
  }

  function provjeriDatumOdlazak(datumPocetak, datumKraj) {
    if (datumKraj) {
      const dolazak = new Date(datumPocetak);
      const odlazak = new Date(datumKraj);
      if (isNaN(dolazak.getTime())) return 'Prvo unesite datum dolaska!';
      if (odlazak <= dolazak) return 'Datum odlazka mora biti nakon datuma dolaska!';
      return '';
    }
    return 'Unesite datum odlaska!';
  }

  function provjeriBrojOsoba(broj, kapacitet) {
    const n = Number(broj);
    if (!Number.isInteger(n) || n < 1) return 'Unesite barem 1 osobu.';
    if (kapacitet && n > kapacitet) return `Maksimalno ${kapacitet} osoba/e.`;
    return '';
  }

  function provjeriUnos() {
    const e = {
      datumPocetak: provjeriDatumDolazak(rezervacija.datumPocetak),
      datumKraj: provjeriDatumOdlazak(rezervacija.datumPocetak, rezervacija.datumKraj),
      brojOsoba: provjeriBrojOsoba(rezervacija.brojOsoba, soba?.kapacitet),
    };
    setErrors(e);
    return Object.values(e).every(err => err === '');
  }

  function handlePotvrdi(e) {
    e.preventDefault();
    setZauzeto({ greska: '' });
    if (!provjeriUnos()) return;
    openModalBtnRef.current?.click();
  }

  function rezerviraj(e) {
    e.preventDefault();
    if (!provjeriUnos()) return;

    createRezervacija(rezervacija)
      .then(() => {

        closeModalBtnRef.current?.click();
        toast.success('Rezervacija izrađena!', {
          autoClose: 2000,
          position: 'bottom-left',
        });
        setTimeout(() => navigator('/'), 2000);
      })
      .catch(error => {
        if (error.response) {
          const status = error.response.status;
          const poruka = error.response.data?.message || 'Greška na serveru.';
          if (status === 409) {
            setZauzeto({ greska: poruka });
          } else if (status === 403) {
            toast.error('Za izradu rezervacije morate biti prijavljeni!', {
              autoClose: 5000,
              position: 'bottom-left',
            });
          } else {
            console.error('Greška kod izrade rezervacije!', poruka);
          }
        }
      });
  }

  const excludeIntervals = zauzetiTermini.map(t => {
    const start = toLocalDate(t.datumPocetak);
    const endExclusive = toLocalDate(t.datumKraj);
    const endInclusive = new Date(endExclusive.getTime() - 86400000);
    return { start, end: endInclusive };
  });

  if (!soba) return <div className='container mt-5'>Učitavanje...</div>;

  const slikeSobe = [
    soba.glavnaSlika,
    ...(Array.isArray(soba.sporedneSlike) ? soba.sporedneSlike : []),
  ];

  return (
    <div className="container py-4">
      <h2 className="text-center mb-4 fw-bold display-5">
        Rezervacija - {soba.hotel.naziv}
      </h2>

      <div className="text-center mb-4 text-muted">
        {soba.hotel.grad.imeGrad}, {soba.hotel.adresa}
      </div>

      <div className="row g-4 align-items-stretch">
        <div className="col-lg-7 d-flex flex-column">
          <div id="sobaCarousel" className="carousel slide mb-4" data-bs-ride="carousel">
            <div className="carousel-inner">
              {slikeSobe.map((slika, index) => (
                <div className={`carousel-item ${index === 0 ? 'active' : ''}`} key={index}>
                  <img
                    src={slika.putanja}
                    alt={`Slika ${index + 1}`}
                    className="d-block w-100"
                    style={{ height: '400px', objectFit: 'cover', borderRadius: '10px' }}
                  />
                </div>
              ))}
            </div>
            <button className="carousel-control-prev" type="button" data-bs-target="#sobaCarousel" data-bs-slide="prev">
              <span className="carousel-control-prev-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Previous</span>
            </button>
            <button className="carousel-control-next" type="button" data-bs-target="#sobaCarousel" data-bs-slide="next">
              <span className="carousel-control-next-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Next</span>
            </button>
          </div>

          <div className="card shadow-lg border-0 h-100">
            <div className="card-body">
              <h4 className="fw-bold mb-3">Detalji sobe</h4>
              <ul className="list-unstyled mb-0">
                <li><strong>Kapacitet:</strong> {soba.kapacitet} osoba/e</li>
                <li><strong>Cijena noćenja:</strong> {soba.cijenaNocenja} €</li>
                <li>
                  <strong>Balkon:</strong>{' '}
                  {soba.balkon ? <i className="bi bi-check2 text-success ms-1"></i> : <i className="bi bi-x text-danger ms-1"></i>}
                </li>
                <li>
                  <strong>Pet friendly:</strong>{' '}
                  {soba.petFriendly ? <i className="bi bi-check2 text-success ms-1"></i> : <i className="bi bi-x text-danger ms-1"></i>}
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div className="col-lg-5 d-flex">
          <div className="card shadow-lg border-0 p-5 w-100 h-100">
            <h4 className="fw-bold mb-5">Podaci o rezervaciji</h4>
            <form>
              <div className='mb-3'>
                <label className='form-label'>Broj sobe</label>
                <input type="text" className='form-control' value={soba.brojSobe} readOnly />
              </div>

              <div className="mb-3">
                <label className="form-label">Broj osoba</label>
                <input
                  type="number"
                  className={`form-control ${errors.brojOsoba ? 'is-invalid' : ''}`}
                  min={1}
                  max={soba.kapacitet}
                  value={rezervacija.brojOsoba}
                  onChange={(e) =>
                    setRezervacija({ ...rezervacija, brojOsoba: Number(e.target.value) })
                  }
                />
                {errors.brojOsoba ? (
                  <div className="invalid-feedback">{errors.brojOsoba}</div>
                ) : (
                  <small className="text-muted">Maksimalno {soba.kapacitet} osoba/e</small>
                )}
              </div>

              <div className="mb-3">
                <label className="form-label">Datum dolaska</label><br />
                <DatePicker
                  selected={startDate}
                  onChange={onChangePocetakDP}
                  selectsStart
                  startDate={startDate}
                  endDate={endDate}
                  minDate={new Date()}
                  excludeDateIntervals={excludeIntervals}
                  dateFormat="yyyy-MM-dd"
                  placeholderText="Odaberite datum dolaska"
                  className={`form-control ${errors.datumPocetak ? 'is-invalid' : ''}`}
                />
                {errors.datumPocetak && <div className='invalid-feedback d-block'>{errors.datumPocetak}</div>}
              </div>

              <div className="mb-3">
                <label className="form-label">Datum odlaska</label><br />
                <DatePicker
                  selected={endDate}
                  onChange={onChangeKrajDP}
                  selectsEnd
                  startDate={startDate}
                  endDate={endDate}
                  minDate={startDate ? addDays(startDate, 1) : new Date()}
                  excludeDateIntervals={excludeIntervals}
                  dateFormat="yyyy-MM-dd"
                  placeholderText="Odaberite datum odlaska"
                  className={`form-control ${errors.datumKraj ? 'is-invalid' : ''}`}
                />
                {errors.datumKraj && <div className='invalid-feedback d-block'>{errors.datumKraj}</div>}
              </div>

              {zauzeto.greska && <div className='alert alert-danger' role='alert'>{zauzeto.greska}</div>}

              <div className="d-flex mt-5">
                <button
                  type='button'
                  className="btn btn-primary btn-lg w-100 me-3"
                  onClick={handlePotvrdi}
                >
                  Potvrdi
                </button>
                <button type="button" className="btn btn-outline-danger btn-lg w-100" onClick={() => navigator(-1)}>
                  Odustani
                </button>

                <button
                  type="button"
                  ref={openModalBtnRef}
                  className="d-none"
                  data-bs-toggle="modal"
                  data-bs-target="#exampleModal"
                >
                  Open
                </button>

                {/* Modal */}
                <div className='modal fade' id='exampleModal' tabIndex={-1} aria-labelledby='exampleModalLabel' aria-hidden='true'>
                  <div className='modal-dialog'>
                    <div className='modal-content'>
                      <div className='modal-header'>
                        <h5 className='modal-title' id='exampleModalLabel'>
                          Rezervacija - <strong>{soba.hotel.naziv} (soba {soba.brojSobe})</strong>
                        </h5>
                        <button
                          type='button'
                          className='btn-close'
                          data-bs-dismiss='modal'
                          aria-label='Close'
                          ref={closeModalBtnRef}
                        ></button>
                      </div>
                      <div className='modal-body'>
                        <p><strong>Broj osoba:</strong> {rezervacija.brojOsoba} osoba/e</p>
                        <p><strong>Datum početka:</strong> {rezervacija.datumPocetak}</p>
                        <p><strong>Datum kraja:</strong> {rezervacija.datumKraj}</p>
                        <hr />
                        <p><strong>Ukupna cijena:</strong> {cijenaRezervacije(rezervacija.datumPocetak, rezervacija.datumKraj, soba.cijenaNocenja)} €</p>
                        <p><strong>Plaćanje:</strong> pri dolasku</p>
                        <hr />
                        <p><strong>Rezervacija na: </strong> {korisnik.ime} {korisnik.prezime}</p>
                        <p><strong>Kontakt: </strong> {korisnik.email} </p>
                      </div>
                      <div className='modal-footer'>
                        <button type='button' className='btn btn-secondary' data-bs-dismiss='modal'>Zatvori</button>
                        <button type='button' className='btn btn-primary' onClick={rezerviraj}>Rezerviraj</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RezervacijaComponent;
