import React, { useEffect, useState } from 'react';
import { getKorisnik, adminDeleteKorisnik, deleteKorisnik } from '../services/KorisnikService';
import { getAktivneRezervacije, getStareRezervacije } from '../services/RezervacijaService';
import { getRecenzijeByIdKorisnik } from '../services/RecenzijaService';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';

const KorisnikDetaljiComponent = () => {
  const [korisnik, setKorisnik] = useState({
    id: '',
    email: '',
    ime: '',
    prezime: '',
  });

  const [recenzije, setRecenzije] = useState([]);

  const [aktivneRez, setAktivne] = useState([]);

  const [stareRez, setStare] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const formatDatum = (datum) => (datum ? new Date(datum).toLocaleDateString('hr-HR') : '');

  const inicijali = (ime = '', prezime = '') =>
    `${(ime[0] || '').toUpperCase()}${(prezime[0] || '').toUpperCase()}`;

  const { idKorisnik } = useParams();

  const navigator = useNavigate();

  const [modalObrisi, setModalObrisi] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const [korisnikRes, recenzijeRes, aktivneRes, stareRes] = await Promise.all([
          getKorisnik(idKorisnik),
          getRecenzijeByIdKorisnik(idKorisnik),
          getAktivneRezervacije(idKorisnik),
          getStareRezervacije(idKorisnik),
        ]);

        setKorisnik({
          id: korisnikRes.data.id,
          ime: korisnikRes.data.ime,
          prezime: korisnikRes.data.prezime,
          email: korisnikRes.data.email,
        });

        setRecenzije(recenzijeRes.data || []);
        setAktivne(aktivneRes.data || []);
        setStare(stareRes.data || []);
      } catch (error) {
        console.error('Greška kod dohvaćanja korisničkih podataka!', error);
      } finally {
        setUcitavanje(false);
      }
    })();
  }, [idKorisnik]);

  const closeModal = () => {
    setModalObrisi(false);
  }

  const openModal = () => {
    setModalObrisi(true);
  }

  if (ucitavanje) {
    return <div className="container py-5 mt-5">Učitavanje...</div>;
  }

  const obrisiKorisnika = async () => {
    if (!modalObrisi) return;

    try {
      await adminDeleteKorisnik(korisnik.id);

      toast.success("Korisnik uspješno obrisan", {
        autoClose: 2000,
        position: 'bottom-left'
      })

      navigator('/admin');
    } catch (e) {
      console.error("Greška kod brisanja korisnika!", e);
    }
  }

  return (
    <div className="container py-5 mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="d-flex align-items-center">
          <div
            className="me-3 d-flex justify-content-center align-items-center rounded-circle bg-primary text-white"
            style={{ width: 56, height: 56, fontWeight: 700 }}
          >
            {inicijali(korisnik.ime, korisnik.prezime)}
          </div>
          <div>
            <h3 className="m-0">
              {korisnik.ime} {korisnik.prezime}
            </h3>
            <div className="text-muted">{korisnik.email}</div>
          </div>
        </div>

        <div className="d-flex gap-2">
          <button className="btn btn-outline-secondary" onClick={() => navigator('/admin')}>
            Natrag
          </button>
          <button className="btn btn-danger" onClick={openModal}>
            Obriši
          </button>
        </div>
      </div>

      {/* AKTIVNE */}
      <div className="card shadow-sm border-0 mb-4">
        <div className="card-body">
          <h5 className="fw-bold mb-3">
            Aktivne rezervacije
            <span className="text-muted fw-normal ms-2">({aktivneRez.length})</span>
          </h5>

          {aktivneRez.length === 0 ? (
            <div className="alert alert-light m-0">Korisnik nema aktivnih rezervacija.</div>
          ) : (
            <div className="table-responsive">
              <table className="table align-middle">
                <thead>
                  <tr>
                    <th>Hotel</th>
                    <th>Soba</th>
                    <th>Osoba/e</th>
                    <th>Dolazak</th>
                    <th>Odlazak</th>
                  </tr>
                </thead>
                <tbody>
                  {aktivneRez.map((rez) => (
                    <tr key={rez.id}>
                      <td>{rez.soba?.hotel?.naziv}</td>
                      <td>{rez.soba?.brojSobe}</td>
                      <td>{rez.brojOsoba}</td>
                      <td>{formatDatum(rez.datumPocetak)}</td>
                      <td>{formatDatum(rez.datumKraj)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* STARE */}
      <div className="card shadow-sm border-0 mb-4">
        <div className="card-body">
          <h5 className="fw-bold mb-3">
            Stare rezervacije
            <span className="text-muted fw-normal ms-2">({stareRez.length})</span>
          </h5>

          {stareRez.length === 0 ? (
            <div className="alert alert-light m-0">Korisnik nema starih rezervacija.</div>
          ) : (
            <div className="table-responsive">
              <table className="table align-middle">
                <thead>
                  <tr>
                    <th>Hotel</th>
                    <th>Soba</th>
                    <th>Osoba/e</th>
                    <th>Dolazak</th>
                    <th>Odlazak</th>
                  </tr>
                </thead>
                <tbody>
                  {stareRez.map((rez) => (
                    <tr key={rez.id}>
                      <td>{rez.soba?.hotel?.naziv}</td>
                      <td>{rez.soba?.brojSobe}</td>
                      <td>{rez.brojOsoba}</td>
                      <td>{formatDatum(rez.datumPocetak)}</td>
                      <td>{formatDatum(rez.datumKraj)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* RECENZIJE */}
      <div className="card shadow-sm border-0">
        <div className="card-body">
          <h5 className="fw-bold mb-3">
            Recenzije
            <span className="text-muted fw-normal ms-2">({recenzije.length})</span>
          </h5>

          {recenzije.length === 0 ? (
            <div className="alert alert-light m-0">Korisnik nema dodanih recenzija.</div>
          ) : (
            recenzije.map((rec) => (
              <div key={rec.id} className="card mb-3 border-0 shadow-sm">
                <div className="card-body">
                  <div className="d-flex justify-content-between flex-wrap gap-2">
                    <div className="fw-semibold">{rec.hotel?.naziv}</div>
                    <div aria-label={`Ocjena ${rec.ocjena ?? 0} od 5`}>
                      {'★'.repeat(rec.ocjena ?? 0)}
                      {'☆'.repeat(Math.max(0, 5 - (rec.ocjena ?? 0)))}
                    </div>
                  </div>

                  {rec.tekst && <p className="mb-2 mt-2">{rec.tekst}</p>}
                  <small className="text-muted">{formatDatum(rec.datum)}</small>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* modal za brisanje korisnika */}
      {modalObrisi && (
        <div
          className='modal fade show d-block'
          tabIndex={-1}
          role='dialog'
          style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
          onClick={closeModal}
        >
          <div
            className='modal-dialog'
            role='document'
            onClick={(e) => e.stopPropagation()}
          >
            <div className='modal-content'>
              <div className='modal-header'>
                <h5 className='modal-title'>Brisanje korisničkog računa</h5>
                <button
                  type='button'
                  className='btn-close'
                  onClick={closeModal}
                  aria-label='Zatvori'
                ></button>
              </div>

              <div className='modal-body'>
                <big>Jeste li sigurni?</big>
                <br /><br />
                <p><b>Korisnički račun:</b> {korisnik.email} - {korisnik.prezime}, {korisnik.ime}</p>
              </div>

              <div className='modal-footer'>
                <button type='button' className='btn btn-secondary' onClick={closeModal}>Odustani</button>
                <button type='button' className='btn btn-primary' onClick={obrisiKorisnika}>Potvrdi</button>
              </div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default KorisnikDetaljiComponent;
