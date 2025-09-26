import React, { useEffect, useState } from 'react';
import { getKorisnik, adminDeleteKorisnik } from '../services/KorisnikService';
import { getAktivneRezervacije, getStareRezervacije, adminDeleteRezervacija, adminUpdateRezervacija, getZauzetiDatumi } from '../services/RezervacijaService';
import { getRecenzijeByIdKorisnik } from '../services/RecenzijaService';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

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

  const inicijali = (ime = '', prezime = '') =>
    `${(ime[0] || '').toUpperCase()}${(prezime[0] || '').toUpperCase()}`;

  const { idKorisnik } = useParams();

  const navigator = useNavigate();

  const [modalObrisi, setModalObrisi] = useState(false);

  const [zauzetiTermini, setZauzetiTermini] = useState([]);
  const [pocetniDatum, setPocetniDatum] = useState(null);
  const [zavrsniDatum, setZavrsniDatum] = useState(null);

  const [odabranaRez, setOdabranaRez] = useState(null);
  const [modalRez, setModalRez] = useState(false);

  const [formaRez, setFormaRez] = useState({
    brojOsoba: 1,
    datumPocetak: '',
    datumKraj: ''
  });

  const [urediRezErr, setUrediRezErr] = useState({
    datumPocetak: '',
    datumKraj: '',
    brojOsoba: ''
  });

  //za prikaz korisnicima, hrvatski datum
  const formatDatum = (datum) =>
    datum ? new Date(datum).toLocaleDateString('hr-HR') : '';

  //dok dobim iz backenda string datum da ga stavim u datePicker kao selected, ili za usporedbe po danima
  const uLokalniDatum = (yyyyMmDd) =>
    yyyyMmDd ? new Date(`${yyyyMmDd}`) : null;

  //za slanje datuma u backend, šaljem kao string, u JS nema LocalDate (bez vremenske zone i vremena)
  const uYMD = (d) =>
    d instanceof Date && !isNaN(d) ? d.toLocaleDateString('sv-SE') : '';

  //za odredivanje minKraj, klonira datum i postavi vrijeme na 00:00 (da usporedba bu cisto po danima bez sata/minuta), doda N dana i vrati novi Date
  const dodajDane = (date, dani) => {
    if (!date) return null;
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    d.setDate(d.getDate() + dani);
    return d;
  };

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

  useEffect(() => {
    if (!(modalRez && odabranaRez)) return;

    //normaliziram datume za formu?
    const poc = uYMD(new Date(odabranaRez.datumPocetak));
    const kraj = uYMD(new Date(odabranaRez.datumKraj));

    setFormaRez({
      brojOsoba: odabranaRez.brojOsoba,
      datumPocetak: poc,
      datumKraj: kraj
    });

    //datePickeru trebaju Date objekti
    setPocetniDatum(uLokalniDatum(poc));
    setZavrsniDatum(uLokalniDatum(kraj));
    setUrediRezErr({ datumPocetak: '', datumKraj: '', brojOsoba: '' });

    (async () => {
      try {
        const res = await getZauzetiDatumi(odabranaRez.soba.id);
        const termini = Array.isArray(res.data) ? res.data : [];
        const bezMoje = termini.filter(t => (t.idRezervacija ?? t.id) !== odabranaRez.id);
        setZauzetiTermini(bezMoje);
      } catch (e) {
        console.error('Greška kod dohvaćanja zauzetih termina!', e);
        setZauzetiTermini([]);
      }
    })();
  }, [modalRez, odabranaRez]);

  const closeModal = () => {
    setModalObrisi(false);
    setModalRez(false);
    setOdabranaRez(null);
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

  const handleRowClick = (rezervacija) => {
    setOdabranaRez(rezervacija);
    setModalRez(true);
  };

  const promjenaForme = (e) =>
    setFormaRez((staro) => ({ ...staro, [e.target.name]: e.target.value }));

  const promjenaPocetka = (date) => {
    setPocetniDatum(date);
    setFormaRez(prev => ({ ...prev, datumPocetak: uYMD(date) }));
    const minKraj = dodajDane(date, 1);
    if (zavrsniDatum && minKraj && zavrsniDatum < minKraj) {
      setZavrsniDatum(null);
      setFormaRez(prev => ({ ...prev, datumKraj: '' }));
    }
    setUrediRezErr(prev => ({ ...prev, datumPocetak: '' }));
  };

  const promjenaKraja = (date) => {
    setZavrsniDatum(date);
    setFormaRez(prev => ({ ...prev, datumKraj: uYMD(date) }));
    setUrediRezErr(prev => ({ ...prev, datumKraj: '' }));
  };

  // validacije
  function provjeriDatumDolaska(datumPocetak) {
    if (datumPocetak) {
      const danas = new Date(); danas.setHours(0, 0, 0, 0);
      const dolazak = new Date(datumPocetak);
      if (dolazak < danas) return 'Datum dolaska ne može biti prije današnjeg datuma!';
      return '';
    }
    return 'Unesite datum dolaska!';
  }

  function provjeriDatumOdlaska(datumPocetak, datumKraj) {
    if (datumKraj) {
      const dolazak = new Date(datumPocetak);
      const odlazak = new Date(datumKraj);
      if (isNaN(dolazak.getTime())) return 'Prvo unesite datum dolaska!';
      if (odlazak <= dolazak) return 'Datum odlaska mora biti nakon datuma dolaska!';
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
      datumPocetak: provjeriDatumDolaska(formaRez.datumPocetak),
      datumKraj: provjeriDatumOdlaska(formaRez.datumPocetak, formaRez.datumKraj),
      brojOsoba: provjeriBrojOsoba(formaRez.brojOsoba, odabranaRez?.soba?.kapacitet)
    };
    setUrediRezErr(e);
    return Object.values(e).every((msg) => msg === '');
  }

  // intervali za blokadu u DatePickeru (checkout dan je slobodan → kraj - 1 dan)
  const zabranjeniIntervali = zauzetiTermini.map(t => {
    const start = uLokalniDatum(t.datumPocetak);
    const endExclusive = uLokalniDatum(t.datumKraj);
    const endInclusive = new Date(endExclusive.getTime() - 86400000);
    return { start, end: endInclusive };
  });

  // UPDATE — poziv na backend + lokalno ažuriranje
  const urediRezervaciju = async () => {
    if (!provjeriUnos()) return;

    const updateDto = {
      brojOsoba: Number(formaRez.brojOsoba),
      datumPocetak: formaRez.datumPocetak,
      datumKraj: formaRez.datumKraj
    };

    try {
      await adminUpdateRezervacija(odabranaRez.id, updateDto);

      toast.success(`Rezervacija ažurirana!`, {
        autoClose: 2000,
        position: 'bottom-left'
      })

      // lokalno ažuriraj listu i odabranu
      setAktivne(stare =>
        stare.map(r => (r.id === odabranaRez.id ? { ...r, ...updateDto } : r))
      );
      setOdabranaRez(prev => prev ? { ...prev, ...updateDto } : prev);

      setModalRez(false);
    } catch (e) {
      console.error('Greška kod ažuriranja rezervacije!', e);
    }
  };

  // DELETE — poziv na backend + lokalno uklanjanje
  const obrisiRezervaciju = async () => {
    if (!odabranaRez) return;
    try {
      await adminDeleteRezervacija(odabranaRez.id);

      // makni iz liste i zatvori modal
      setAktivne(stare => stare.filter(r => r.id !== odabranaRez.id));

      toast.success("Rezervacija izbrisana!", {
        autoClose: 2000,
        position: 'bottom-left'
      })

      setModalRez(false);
      setOdabranaRez(null);

    } catch (e) {
      console.error('Greška kod brisanja rezervacije!', e);
    }
  };

  return (
    <div className="container py-5">
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
              <table className="table table-hover align-middle">
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
                    <tr
                      key={rez.id}
                      onClick={() => handleRowClick(rez)}
                      style={{ cursor: 'pointer' }}
                      role="button"
                      tabIndex={0}
                    >
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

      {/* Modal za uređivanje rezervacije */}
      {
        modalRez && odabranaRez && (
          <div
            className="modal fade show d-block"
            tabIndex={-1}
            role="dialog"
            style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
            onClick={closeModal}
          >
            <div
              className="modal-dialog modal-lg  modal-dialog-centered"
              role="document"
              onClick={(e) => e.stopPropagation()} // da se ne zatvori dok se klikne unutra
            >
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">
                    Rezervacija - {odabranaRez.soba.hotel.naziv} (soba {odabranaRez.soba.brojSobe})
                  </h5>
                  <button
                    type="button"
                    className="btn-close"
                    aria-label="Zatvori"
                    onClick={closeModal}
                  ></button>
                </div>

                <div className="modal-body">
                  <form className="row g-3">
                    <div className="col-sm-4">
                      <label className="form-label">Osoba/e</label>
                      <input
                        type="number"
                        min={1}
                        max={odabranaRez.soba.kapacitet}
                        name="brojOsoba"
                        className={`form-control ${urediRezErr.brojOsoba ? 'is-invalid' : ''}`}
                        value={formaRez.brojOsoba}
                        onChange={promjenaForme}
                      />
                      {urediRezErr.brojOsoba ? (
                        <div className="invalid-feedback d-block">{urediRezErr.brojOsoba}</div>
                      ) : (
                        <small className='text-muted'>Maksimalno {odabranaRez.soba.kapacitet} osoba/e</small>
                      )}

                    </div>

                    <div className="col-sm-4">
                      <label className="form-label">Dolazak</label>
                      <br />
                      <DatePicker
                        selected={pocetniDatum}
                        onChange={promjenaPocetka}
                        selectsStart
                        startDate={pocetniDatum}
                        endDate={zavrsniDatum}
                        minDate={new Date()}
                        excludeDateIntervals={zabranjeniIntervali}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Odaberite datum dolaska"
                        className={`form-control ${urediRezErr.datumPocetak ? 'is-invalid' : ''}`}
                      />
                      {urediRezErr.datumPocetak && (
                        <div className="invalid-feedback d-block">{urediRezErr.datumPocetak}</div>
                      )}
                    </div>

                    <div className="col-sm-4">
                      <label className="form-label">Odlazak</label>
                      <br />
                      <DatePicker
                        selected={zavrsniDatum}
                        onChange={promjenaKraja}
                        selectsEnd
                        startDate={pocetniDatum}
                        endDate={zavrsniDatum}
                        minDate={pocetniDatum ? dodajDane(pocetniDatum, 1) : new Date()}
                        excludeDateIntervals={zabranjeniIntervali}
                        dateFormat="yyyy-MM-dd"
                        placeholderText="Odaberite datum odlaska"
                        className={`form-control ${urediRezErr.datumKraj ? 'is-invalid' : ''}`}
                      />
                      {urediRezErr.datumKraj && (
                        <div className="invalid-feedback d-block">{urediRezErr.datumKraj}</div>
                      )}
                    </div>
                  </form>
                </div>

                <div className="modal-footer">
                  <button className="btn btn-outline-secondary" onClick={closeModal}>
                    Odustani
                  </button>
                  <button className="btn btn-outline-primary" onClick={urediRezervaciju}>
                    Uredi
                  </button>
                  <button className="btn btn-outline-danger" onClick={obrisiRezervaciju}>
                    Obriši
                  </button>
                </div>
              </div>
            </div>
          </div>
        )
      }

    </div>
  );
};

export default KorisnikDetaljiComponent;
