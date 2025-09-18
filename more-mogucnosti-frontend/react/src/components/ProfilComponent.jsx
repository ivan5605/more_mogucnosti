import React, { useState, useEffect } from 'react';
import { prijavljeni } from '../services/AuthService';
import { getRecenzijeKorisnika, upsertRecenzija, deleteRecenzija } from '../services/RecenzijaService';
import { getRezervacijeKorisnika, getZauzetiDatumi, updateRezervacija, deleteRezervacija } from '../services/RezervacijaService';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { deleteKorisnik, updateKorisnik, updateLozinka } from '../services/KorisnikService';
import { useAuth } from '../auth/AuthContext';
import { data, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const ProfilComponent = () => {
  const [korisnik, setKorisnik] = useState({
    ime: '',
    prezime: '',
    email: ''
  });

  const [zauzetiTermini, setZauzetiTermini] = useState([]);
  const [pocetniDatum, setPocetniDatum] = useState(null);
  const [zavrsniDatum, setZavrsniDatum] = useState(null);

  const [rezervacije, setRezervacije] = useState([]);
  const [recenzije, setRecenzije] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const [odabranaRez, setOdabranaRez] = useState(null);
  const [modalRez, setModalRez] = useState(false);

  const [promjenaRez, setPromjenaRez] = useState(false);

  // forma u modalu (uređivanje rezervacije)
  const [formaRez, setFormaRez] = useState({
    brojOsoba: 1,
    datumPocetak: '',
    datumKraj: ''
  });

  const [greske, setGreske] = useState({
    datumPocetak: '',
    datumKraj: '',
    brojOsoba: ''
  });

  const [odabranaRec, setOdabranaRec] = useState(null);
  const [modalRec, setModalRec] = useState(false);

  const [formaRec, setFormaRec] = useState({
    ocjena: '',
    tekst: ''
  });

  const [urediRecErr, setUrediRecErr] = useState({
    ocjena: '',
    tekst: ''
  });

  const [modalObrisi, setModalObrisi] = useState(false);
  const [lozinkaZaBrisanje, setLozinkaZaBrisanje] = useState('');
  const [brisanjeErr, setBrisanjeErr] = useState('');

  const { logout } = useAuth();
  const navigator = useNavigate();

  const [modalUredi, setModalUredi] = useState(false);
  const [formaUredi, setFormaUredi] = useState({
    ime: '',
    prezime: '',
    email: ''
  });
  const [urediErr, setUrediErr] = useState({
    ime: '',
    prezime: '',
    email: ''
  })

  const [modalPromjenaLoz, setModalPromjenaLoz] = useState(false);
  const [formaPromjenaLoz, setFormaPromjenLoz] = useState({
    staraLozinka: '',
    novaLozinka: '',
    novaLozinkaPotvrda: ''
  })
  const [promjenaLozErr, setPromjenaLozErr] = useState({
    staraLozinka: '',
    novaLozinka: '',
    novaLozinkaPotvrda: ''
  })

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

  // inicijalni dohvat
  //async uvijek vraca Promise, await pauzira izvrsenje unutar te async funkcije dok se Promise ne rijesi
  useEffect(() => {
    (async () => {
      try {
        const [korisnikRes, rezervacijeRes, recenzijeRes] = await Promise.all([ //pokrese vise Promise-a i ceka da se svi zavrse, vraca array rezultata istim redoslijedom, ako i jedan padne baca gresku (fail-fast)
          prijavljeni(),
          getRezervacijeKorisnika(),
          getRecenzijeKorisnika()
        ]);

        setKorisnik({
          ime: korisnikRes.data.ime,
          prezime: korisnikRes.data.prezime,
          email: korisnikRes.data.email
        });

        //provjera je li vrijednost lista/array
        setRezervacije(Array.isArray(rezervacijeRes.data) ? [...rezervacijeRes.data] : []);
        setRecenzije(Array.isArray(recenzijeRes.data) ? [...recenzijeRes.data] : []);
      } catch (error) {
        console.error('Greška kod dohvaćanja profila/rezervacija/recenzija:', error);
      } finally {
        setUcitavanje(false);
      }
    })();
  }, [promjenaRez]); //pokrece se jednom na mount

  useEffect(() => {
    if (!(modalUredi)) return;

    setFormaUredi({
      ime: korisnik.ime,
      prezime: korisnik.prezime,
      email: korisnik.email
    })
  }, [modalUredi])

  // priprema modala
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
    setGreske({ datumPocetak: '', datumKraj: '', brojOsoba: '' });

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
  }, [modalRez, odabranaRez]); //pokrece se dok otvorim modal, i promjenim odabranu rezervaciju dok je modalRez true

  useEffect(() => {
    if (!(modalRec && odabranaRec)) return;

    setFormaRec({
      ocjena: String(odabranaRec.ocjena ?? ''),
      tekst: odabranaRec.tekst ?? ''
    });
    setUrediRecErr({ ocjena: '', tekst: '' });
  }, [modalRec, odabranaRec]);


  if (ucitavanje) {
    return <div className="container py-5 mt-5">Učitavanje…</div>;
  }

  const handleUrediClick = () => {
    setModalUredi(true);
  }

  const handleBrisanjeClick = () => {
    setModalObrisi(true);
  }

  const handlePromjenaLozClick = () => {
    setModalPromjenaLoz(true);
  }

  const handleRowClick = (rezervacija) => {
    setOdabranaRez(rezervacija);
    setModalRez(true);
  };

  const promjenaForme = (e) =>
    setFormaRez((staro) => ({ ...staro, [e.target.name]: e.target.value }));

  const promjenaFormeUredi = (e) =>
    setFormaUredi((staro) => ({ ...staro, [e.target.name]: e.target.value }));

  const promjenaFormePromjenaLoz = (e) => {
    const { name, value } = e.target;
    setFormaPromjenLoz((staro) => ({ ...staro, [name]: value }));
    setPromjenaLozErr((stare) => ({ ...stare, [name]: '' }));
  };

  const promjenaFormeRec = (e) => {
    const { name, value } = e.target;
    setFormaRec((staro) => ({ ...staro, [name]: value }));
    setUrediRecErr((stare) => ({ ...stare, [name]: '' }));
  };


  const closeModal = () => {
    setModalObrisi(false);
    setModalRez(false);
    setOdabranaRez(null);
    setLozinkaZaBrisanje("");
    setBrisanjeErr("");
    setModalUredi(false);
    setModalPromjenaLoz(false);
    setPromjenaLozErr({ staraLozinka: '', novaLozinka: '', novaLozinkaPotvrda: '' });
    setFormaPromjenLoz({ staraLozinka: '', novaLozinka: '', novaLozinkaPotvrda: '' });
    setModalRec(false);
    setFormaRec({ ocjena: '', tekst: '' });
    setUrediRecErr({ ocjena: '', tekst: '' });
  };

  // DatePicker promjene
  const promjenaPocetka = (date) => {
    setPocetniDatum(date);
    setFormaRez(prev => ({ ...prev, datumPocetak: uYMD(date) }));
    const minKraj = dodajDane(date, 1);
    if (zavrsniDatum && minKraj && zavrsniDatum < minKraj) {
      setZavrsniDatum(null);
      setFormaRez(prev => ({ ...prev, datumKraj: '' }));
    }
    setGreske(prev => ({ ...prev, datumPocetak: '' }));
  };

  const promjenaKraja = (date) => {
    setZavrsniDatum(date);
    setFormaRez(prev => ({ ...prev, datumKraj: uYMD(date) }));
    setGreske(prev => ({ ...prev, datumKraj: '' }));
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
    setGreske(e);
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
      await updateRezervacija(odabranaRez.id, updateDto);

      toast.success(`Rezervacija ažurirana!`, {
        autoClose: 2000,
        position: 'bottom-left'
      })

      {
        promjenaRez ? (
          setPromjenaRez(false)
        ) : (
          setPromjenaRez(true)
        )
      };

      // lokalno ažuriraj listu i odabranu
      setRezervacije(stare =>
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
      await deleteRezervacija(odabranaRez.id);

      // makni iz liste i zatvori modal
      setRezervacije(stare => stare.filter(r => r.id !== odabranaRez.id));

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

  function provjeriLozinkaBrisanje(lozinka) {
    if (!lozinka.trim()) {
      setBrisanjeErr("Unesite lozinku!")
      return false;
    }
    setBrisanjeErr("");
    return true;
  }

  function deleteProfil(e) {
    e.preventDefault();

    if (provjeriLozinkaBrisanje(lozinkaZaBrisanje)) {
      deleteKorisnik(lozinkaZaBrisanje).then(response => {
        logout();
      }).catch(error => {
        setBrisanjeErr(error.response.data.message);
      })
    }
  }

  function provjeriUrediIme(ime) {
    if (!ime) {
      return 'Unesite ime!';
    }
    return '';
  }

  function provjeriUrediPrezime(prezime) {
    if (!prezime) {
      return 'Unesite prezime!';
    }
    return '';
  }

  function provjeriUrediEmail(email) {
    if (!email) {
      return "Unesite email!";
    } else if (!/^[^\s@]+@[^\s@]+\.[A-Za-z]{2,}$/.test(email)) {
      return "Neispravna email adresa!";
    } else {
      return "";
    }
  }

  function provjeriUredjenKorisnik(ev) {


    const e = {
      ime: provjeriUrediIme(formaUredi.ime),
      prezime: provjeriUrediPrezime(formaUredi.prezime),
      email: provjeriUrediEmail(formaUredi.email)
    };
    setUrediErr(e);
    return Object.values(e).every((msg) => msg === '');
  }

  const urediProfil = async () => {
    if (!provjeriUredjenKorisnik()) return;

    const dto = {
      ime: formaUredi.ime,
      prezime: formaUredi.prezime,
      email: formaUredi.email
    };

    try {
      await updateKorisnik(dto);

      setKorisnik({
        ime: dto.ime,
        prezime: dto.prezime,
        email: dto.email
      })

      setModalUredi(false);

      toast.success('Profil uspješno uređen!', {
        autoClose: 2000,
        position: 'bottom-left'
      })
    } catch (e) {
      if (e.response) {
        const status = e.response.status;
        const poruka = e.response.data.message || "Greška"

        if (status === 409) {
          setUrediErr(prev => ({ ...prev, email: poruka }))
        } else {
          console.error('Greška kod ažuriranja profila!', e);
        }
      }
    }
  }

  function provjeriStaraLozinka(staraLozinka) {
    if (!staraLozinka) return "Unesite svoju trenutnu lozinku!"
    return "";
  }

  function provjeriNovaLozinka(novaLozinka) {
    if (!novaLozinka.trim()) {
      return "Unesite lozinku!"
    } else if (novaLozinka.length < 8) {
      return "Lozinka mora imati najmanje 8 znakova!"
    } else if (!/[A-Z]/.test(novaLozinka)) {
      return "Lozinka mora imati barem jedno veliko slovo!"
    } else if (!/\d/.test(novaLozinka)) {
      return "Lozinka mora imati barem jedan broj!"
    }
    return "";
  }

  function provjeriNovaLozinkaPotvrda(novaLozinka, novaLozinkaPotvrda) {
    if (!novaLozinkaPotvrda) return "Unesite lozinku za potvrdu!";

    return novaLozinka === novaLozinkaPotvrda ? "" : "Lozinke se ne podudaraju!";
  }

  function provjeriUnosPromjenaLoz() {
    const e = {
      staraLozinka: provjeriStaraLozinka(formaPromjenaLoz.staraLozinka),
      novaLozinka: provjeriNovaLozinka(formaPromjenaLoz.novaLozinka),
      novaLozinkaPotvrda: provjeriNovaLozinkaPotvrda(
        formaPromjenaLoz.novaLozinka,
        formaPromjenaLoz.novaLozinkaPotvrda
      )
    }

    setPromjenaLozErr(e);

    return Object.values(e).every(error => error === "");
  }

  const promjenaLozinke = async () => {
    if (!provjeriUnosPromjenaLoz()) return;

    const dto = {
      staraLozinka: formaPromjenaLoz.staraLozinka,
      novaLozinka: formaPromjenaLoz.novaLozinka,
      novaLozinkaPotvrda: formaPromjenaLoz.novaLozinkaPotvrda
    }

    try {
      await updateLozinka(dto);

      setModalPromjenaLoz(false);

      logout();

      toast.success("Lozinka uspješno promijenjena!", {
        autoClose: 3000,
        position: 'bottom-left'
      })
    } catch (e) {
      if (e.response) {
        const status = e.response.status;
        const poruka = e.response.data.message || "Greška"

        if (status === 400) {
          setPromjenaLozErr(prev => ({ ...prev, staraLozinka: poruka }))
        } else {
          console.error('Greška', e);
        }
      }
    }
  }

  const handleRecenzijaClick = (recenzija) => {
    setOdabranaRec(recenzija);
    setModalRec(true);
  }

  const Star = ({ filled }) => (
    <i className={`${filled ? 'fas' : 'far'} fa-star ${filled ? 'text-warning' : 'text-muted'}`} aria-hidden="true"></i>
  );



  const obrisiRecenziju = async () => {
    if (!odabranaRec) return;

    try {
      await (deleteRecenzija(odabranaRec.id));

      setRecenzije(stare => stare.filter(r => r.id !== odabranaRec.id));
      setModalRec(false);
      setOdabranaRec(null);

      toast.success("Recenzija izbrisana!", {
        autoClose: 2000,
        position: 'bottom-left'
      })
    } catch (e) {

    }
  }

  function provjeriOcjena(ocjena) {
    if (!ocjena) return 'Unesite ocjenu (1–5)!';
    const n = Number(ocjena);
    if (!Number.isInteger(n) || n < 1 || n > 5) return 'Ocjena mora biti cijeli broj 1–5.';
    return '';
  }

  function provjeriTekst(tekst) {
    if (!tekst.trim()) return 'Unesite tekst recenzije!';
    if (tekst.trim().length < 5) return 'Tekst je prekratak (min 5 znakova).';
    return '';
  }

  function provjeriRecenziju() {
    const e = {
      ocjena: provjeriOcjena(formaRec.ocjena),
      tekst: provjeriTekst(formaRec.tekst)
    }
    setUrediRecErr(e);
    return Object.values(e).every((msg) => msg === '');
  }

  const urediRecenziju = async () => {
    if (!provjeriRecenziju()) return;

    const updateDto = {
      ocjena: Number(formaRec.ocjena),
      tekst: formaRec.tekst
    }

    try {
      await upsertRecenzija(odabranaRec.hotel.id, updateDto);

      toast.success(`Recenzija ažurirana!`, {
        autoClose: 2000,
        position: 'bottom-left'
      })

      setRecenzije(stare => stare.map(r => (r.id === odabranaRec.id ? { ...r, ...updateDto } : r)));
      setOdabranaRec(prev => prev ? { ...prev, ...updateDto } : prev);
      setModalRec(false);
    } catch (e) {
      console.error('Greška kod ažuriranja recenzije!', e);

    }
  }

  return (
    <div className="container py-5 mt-5">
      {/* Korisnički podaci */}
      <div className="d-flex justify-content-between align-items-start mb-4">
        <div className='d-flex align-items-center'>
          <div
            className="me-3 d-flex justify-content-center align-items-center rounded-circle bg-primary text-white"
            style={{ width: 56, height: 56, fontWeight: 700 }}
          >
            {`${korisnik.ime?.[0] ?? ''}${korisnik.prezime?.[0] ?? ''}`.toUpperCase()}
          </div>
          <div>
            <h3 className="m-0">
              {korisnik.ime} {korisnik.prezime}
            </h3>
            <div className="text-muted">{korisnik.email}</div>
          </div>
        </div>

        <div className='dropdown'>
          <button className='btn btn-outline-secondary dropdown-toggle' type='button' id='postavkeRacuna' data-bs-toggle='dropdown' aria-expanded='false'>
            Postavke
          </button>
          <ul className='dropdown-menu' aria-labelledby='postavkeRacuna'>
            <li><a className='dropdown-item' onClick={handleUrediClick} style={{ cursor: 'pointer' }}>Uredi profil</a></li>
            <li><a className='dropdown-item' onClick={handlePromjenaLozClick} style={{ cursor: 'pointer' }} > Promjena lozinke</a></li>
            <li><a className='dropdown-item' onClick={handleBrisanjeClick} style={{ cursor: 'pointer' }}>Brisanje računa</a></li>
            <li><a className='dropdown-item' onClick={logout} style={{ cursor: 'pointer' }}>Odjava</a></li>
          </ul>
        </div>
      </div>

      {/* Rezervacije */}
      <div className="card shadow-sm border-0 mb-4 ">
        <div className="card-body">
          <h5 className="fw-bold mb-3">Moje rezervacije</h5>

          {rezervacije.length === 0 ? (
            <div className="alert alert-light m-0">Nemate još niti jednu rezervaciju.</div>
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
                    <th>Noćenja</th>
                    <th>Ukupna cijena</th>
                  </tr>
                </thead>
                <tbody>
                  {rezervacije.map((rez) => (
                    <tr
                      key={rez.id}
                      onClick={() => handleRowClick(rez)}
                      style={{ cursor: 'pointer' }}
                      role="button"
                      tabIndex={0}
                    >
                      <td>{rez.soba.hotel.naziv}</td>
                      <td>{rez.soba.brojSobe}</td>
                      <td>{rez.brojOsoba}</td>
                      <td>{formatDatum(rez.datumPocetak)}</td>
                      <td>{formatDatum(rez.datumKraj)}</td>
                      <td>{rez.brojNocenja}</td>
                      <td>{rez.ukupnaCijena} €</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          <small className='text-muted'>Kliknite na rezervaciju za uređivanje ili brisanje.</small>
        </div>
      </div>

      {/* Recenzije */}
      <div className="card shadow-sm border-0">
        <div className="card-body">
          <h5 className="fw-bold mb-3">Moje recenzije</h5>

          {recenzije.length === 0 ? (
            <div className="alert alert-light m-0">Niste još ostavili recenziju.</div>
          ) : (
            recenzije.map((rec) => (
              <div key={rec.id} className="card mb-4 border-0 shadow-sm" style={{ cursor: 'pointer' }} onClick={() => handleRecenzijaClick(rec)}>
                <div className="card-body">
                  <div className="d-flex justify-content-between flex-wrap gap-2">
                    <div className="fw-semibold">
                      {rec.hotel.naziv}
                    </div>
                    <div aria-label={`Ocjena ${rec.ocjena} od 5`}>
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

      {/* modal za recenzije */}

      {modalRec && odabranaRec && (
        <div
          className='modal fade show d-block'
          tabIndex={-1}
          role='dialog'
          style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
          onClick={closeModal}
        >
          <div
            className='modal-dialog modal-lg modal-dialog-centered'
            role='document'
            onClick={(e) => e.stopPropagation()}
          >
            <div className='modal-content'>
              <div className='modal-header'>
                <h5 className='modal-title'>
                  Uredi recenziju - {odabranaRec.hotel.naziv}
                </h5>
                <button
                  type='button'
                  className='btn-close'
                  aria-label='Zatvori'
                  onClick={closeModal}
                ></button>
              </div>

              <div className='modal-body'>
                <form className='row g-3'>
                  <div className='col-sm-6'>
                    <label className='form-label'>Ocjena</label>
                    <div role='radiogroup' aria-label='Ocjena' className='mb-2'>
                      {[1, 2, 3, 4, 5].map((n) => (
                        <div className='form-check form-check-inline' key={n}>
                          <input
                            className='form-check-input'
                            type='radio'
                            name='ocjena'
                            id={`ocjena-${n}`}
                            value={n}
                            checked={String(formaRec.ocjena) === String(n)}
                            onChange={promjenaFormeRec}
                          />
                          <label className='form-check-label' htmlFor={`ocjena-${n}`}>
                            {n}
                          </label>
                        </div>
                      ))}
                    </div>

                    <div
                      style={{ height: 'auto', width: '280px', border: 'none' }}
                      className={`form-control ${urediRecErr.ocjena ? 'is-invalid' : ''}`}
                      aria-hidden='true'
                    >
                      {[1, 2, 3, 4, 5].map((n) => (
                        <span key={`star-${n}`} className='me-4 fs-5'>
                          <Star filled={Number(formaRec.ocjena) >= n} />
                        </span>
                      ))}
                    </div>
                    {urediRecErr.ocjena && (
                      <div className='invalid-feedback d-block'>{urediRecErr.ocjena}</div>
                    )}
                  </div>

                  <div className='col-sm-6'>
                    <label className='form-label'>Tekst:</label>
                    <br />
                    <textarea
                      name='tekst'
                      id='tekst'
                      className={`form-control ${urediRecErr.tekst ? 'is-invalid' : ''}`}
                      placeholder='Što mislite o ovom hotelu?'
                      rows={4}
                      maxLength={1000}
                      value={formaRec.tekst}
                      onChange={promjenaFormeRec}
                      onBlur={() =>
                        setUrediRecErr((prev) => ({
                          ...prev,
                          tekst: provjeriTekst(formaRec.tekst)
                        }))
                      }
                    />
                    {urediRecErr.tekst && (
                      <div className='invalid-feedback d-block'>{urediRecErr.tekst}</div>
                    )}
                  </div>
                </form>
              </div>

              <div className='modal-footer'>
                <button className="btn btn-outline-secondary" onClick={closeModal}>
                  Odustani
                </button>
                <button className="btn btn-outline-primary" onClick={urediRecenziju}>
                  Uredi
                </button>
                <button className="btn btn-outline-danger" onClick={obrisiRecenziju}>
                  Obriši
                </button>
              </div>

            </div>
          </div>
        </div >
      )}

      {/* modal za promjenu lozinke */}
      {
        modalPromjenaLoz && (
          <div
            className='modal fade show d-block'
            tabIndex={-1}
            role='dialog'
            style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
            onClick={closeModal}
          >
            <div
              className='modal-dialog modal-lg modal-dialog-centered'
              role='document'
              onClick={(e) => e.stopPropagation()}
            >
              <div className='modal-content'>
                <div className='modal-header'>
                  <h5 className='modal-title'>
                    Promjena lozinke
                  </h5>
                  <button
                    type='button'
                    className='btn-close'
                    aria-label='Zatvori'
                    onClick={closeModal}
                  ></button>
                </div>

                <div className='modal-body'>
                  <form className='row g-3'>
                    <div className='col-sm-4'>
                      <label className='form-label'>Trenutna lozinka:</label>
                      <input
                        type="password"
                        name="staraLozinka"
                        className={`form-control ${promjenaLozErr.staraLozinka ? 'is-invalid' : ''}`}
                        placeholder='Trenutna lozinka:'
                        value={formaPromjenaLoz.staraLozinka}
                        onChange={promjenaFormePromjenaLoz}
                      />
                      {promjenaLozErr.staraLozinka && <div className='invalid-feedback d-block'>{promjenaLozErr.staraLozinka}</div>}
                    </div>
                    <div className='col-sm-4'>
                      <label className='form-label'>Nova lozinka:</label>
                      <input
                        type="password"
                        name="novaLozinka"
                        className={`form-control ${promjenaLozErr.novaLozinka ? 'is-invalid' : ''}`}
                        placeholder='Nova lozinka:'
                        value={formaPromjenaLoz.novaLozinka}
                        onChange={promjenaFormePromjenaLoz}
                      />
                      {promjenaLozErr.novaLozinka && <div className='invalid-feedback d-block'>{promjenaLozErr.novaLozinka}</div>}
                    </div>
                    <div className='col-sm-4'>
                      <label className='form-label'>Potvrdi novu lozinku:</label>
                      <input
                        type="password"
                        name="novaLozinkaPotvrda"
                        className={`form-control ${promjenaLozErr.novaLozinkaPotvrda ? 'is-invalid' : ''}`}
                        placeholder='Nova lozinka (potvrda):'
                        value={formaPromjenaLoz.novaLozinkaPotvrda}
                        onChange={promjenaFormePromjenaLoz}
                      />
                      {promjenaLozErr.novaLozinkaPotvrda && <div className='invalid-feedback d-block'>{promjenaLozErr.novaLozinkaPotvrda}</div>}
                    </div>
                  </form>
                </div>

                <div className='modal-footer'>
                  <button className="btn btn-outline-secondary" onClick={closeModal}>
                    Odustani
                  </button>
                  <button type='submit' className="btn btn-outline-primary" onClick={promjenaLozinke}>
                    Potvrdi
                  </button>
                </div>

              </div>
            </div>
          </div>
        )
      }

      {/* modal za uredivanje korisnickog racuna */}
      {
        modalUredi && (
          <div
            className='modal fade show d-block'
            tabIndex={-1}
            role='dialog'
            style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
            onClick={closeModal}
          >
            <div
              className='modal-dialog modal-lg modal-dialog-centered'
              role='document'
              onClick={(e) => e.stopPropagation()}
            >
              <div className='modal-content'>
                <div className='modal-header'>
                  <h5 className='modal-title'>
                    Uredi račun
                  </h5>
                  <button
                    type='button'
                    className='btn-close'
                    aria-label='Zatvori'
                    onClick={closeModal}
                  ></button>
                </div>

                <div className='modal-body'>
                  <form className='row g-3'>
                    <div className='col-sm-4'>
                      <label className='form-label'>Ime</label>
                      <input
                        type="text"
                        name='ime'
                        className={`form-control ${urediErr.ime ? 'is-invalid' : ''}`}
                        value={formaUredi.ime}
                        onChange={promjenaFormeUredi}
                      />
                      {urediErr.ime && <div className='invalid-feedback d-block'>{urediErr.ime}</div>}
                    </div>
                    <div className='col-sm-4'>
                      <label className='form-label'>Prezime</label>
                      <input
                        type="text"
                        name='prezime'
                        className={`form-control ${urediErr.prezime ? 'is-invalid' : ''}`}
                        value={formaUredi.prezime}
                        onChange={promjenaFormeUredi}
                      />
                      {urediErr.prezime && <div className='invalid-feedback d-block'>{urediErr.prezime}</div>}
                    </div>
                    <div className='col-sm-4'>
                      <label className='form-label'>Email</label>
                      <input
                        type="text"
                        name='email'
                        className={`form-control ${urediErr.email ? 'is-invalid' : ''}`}
                        value={formaUredi.email}
                        onChange={promjenaFormeUredi}
                      />
                      {urediErr.email && <div className='invalid-feedback d-block'>{urediErr.email}</div>}
                    </div>
                  </form>
                </div>

                <div className='modal-footer'>
                  <button className="btn btn-outline-secondary" onClick={closeModal}>
                    Odustani
                  </button>
                  <button className="btn btn-outline-primary" onClick={urediProfil}>
                    Potvrdi
                  </button>
                </div>

              </div>
            </div>
          </div>
        )
      }

      {/* modal za brisanje korisničkog računa */}
      {
        modalObrisi && (
          <div
            className='modal fade show d-block'
            tabIndex={-1}
            role='dialog'
            style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
            onClick={closeModal}
          >
            <div
              className='modal-dialog modal-lg modal-dialog-centered'
              role='document'
              onClick={(e) => e.stopPropagation()}
            >
              <div className='modal-content'>
                <div className='modal-header'>
                  <h5 className='modal-title'>
                    Brisanje računa - {korisnik.email}
                  </h5>
                  <button
                    type='button'
                    className='btn-close'
                    aria-label='Zatvori'
                    onClick={closeModal}
                  ></button>
                </div>

                <div className='modal-body'>
                  <form className='row g-3'>
                    <div className='col-sm-6'>
                      <label className='form-label'>Lozinka:</label>
                      <input
                        type="password"
                        name='lozinkaZaBrisanje'
                        className={`form-control ${brisanjeErr ? 'is-invalid' : ''}`}
                        placeholder='Unesite lozinku'
                        value={lozinkaZaBrisanje}
                        onChange={(e) => setLozinkaZaBrisanje(e.target.value)}
                      />
                      {brisanjeErr && <div className='invalid-feedback d-block'>{brisanjeErr}</div>}
                    </div>
                  </form>
                </div>

                <div className='modal-footer'>
                  <button className='btn btn-outline-secondary' onClick={closeModal}>
                    Odustani
                  </button>
                  <button className='btn btn-danger' onClick={deleteProfil}>
                    Obriši račun
                  </button>
                </div>

              </div>
            </div>
          </div>
        )
      }

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
                        className={`form-control ${greske.brojOsoba ? 'is-invalid' : ''}`}
                        value={formaRez.brojOsoba}
                        onChange={promjenaForme}
                      />
                      {greske.brojOsoba ? (
                        <div className="invalid-feedback d-block">{greske.brojOsoba}</div>
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
                        className={`form-control ${greske.datumPocetak ? 'is-invalid' : ''}`}
                      />
                      {greske.datumPocetak && (
                        <div className="invalid-feedback d-block">{greske.datumPocetak}</div>
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
                        className={`form-control ${greske.datumKraj ? 'is-invalid' : ''}`}
                      />
                      {greske.datumKraj && (
                        <div className="invalid-feedback d-block">{greske.datumKraj}</div>
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

    </div >
  );
};

export default ProfilComponent;
