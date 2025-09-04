import React, { useState, useEffect } from 'react';
import { prijavljeni } from '../services/AuthService';
import { getRecenzijeKorisnika } from '../services/RecenzijaService';
import { getRezervacijeKorisnika } from '../services/RezervacijaService';

const ProfilComponent = () => {
  const [korisnik, setKorisnik] = useState({
    ime: '',
    prezime: '',
    email: ''
  });

  const [rezervacije, setRezervacije] = useState([]);

  const [recenzije, setRecenzije] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const formatDatum = (datum) => (datum ? new Date(datum).toLocaleDateString('hr-HR') : '');
  //napravi novi datum, i formatira u lokalni datum za hrvatsku regiju

  //async funkcija uvijek vraća promise, a onda morem koristiti i await da pričekam rezultat promise-a
  //čitljivije, urednije, dok nekaj čekam, ne koristim then()
  //promise - rezultat ne dolazi odmah, ali došel bude 100% ili greska naravno
  useEffect(() => {
    (async () => {
      try {
        const [korisnikRes, rezervacijeRes, recenzijeRes] = await Promise.all([
          prijavljeni(),
          getRezervacijeKorisnika(),
          getRecenzijeKorisnika()
        ])

        setKorisnik({
          ime: korisnikRes.data.ime,
          prezime: korisnikRes.data.prezime,
          email: korisnikRes.data.email
        });

        // sortiraj rezervacije po datumu dolaska silazno (novije prve)
        const rezervacije = Array.isArray(rezervacijeRes.data) ? [...rezervacijeRes.data] : []; //provjerava jel polje, ako je, radi se kopija
        setRezervacije(rezervacije); //sort sam prolazi kroz sve parove elemenata, samo ovisi jel stavim a-b ili b-a

        // sortiraj recenzije po datumu silazno (novije prve)
        const recenzije = Array.isArray(recenzijeRes.data) ? [...recenzijeRes.data] : [];
        setRecenzije(recenzije);
      } catch (error) {
        console.error('Greška kod dohvaćanja profila/rezervacija/recenzija:', error);
      } finally {
        setUcitavanje(false); //uvijek se izvrsi bez obzira jel uspjesno ili greška, iskljucivam loading indikator
      }
    })();
  }, []);

  if (ucitavanje) {
    return <div className="container py-5 mt-5">Učitavanje…</div>;
  }

  return (
    <div className="container py-5 mt-5">
      {/* Korisnicki podaci */}
      <div className="d-flex align-items-center mb-4">
        <div className="me-3 d-flex justify-content-center align-items-center rounded-circle bg-primary text-white"
          style={{ width: 56, height: 56, fontWeight: 700 }}>
          {`${korisnik.ime?.[0] ?? ''}${korisnik.prezime?.[0] ?? ''}`.toUpperCase()}
        </div>
        <div>
          <h3 className="m-0">{korisnik.ime} {korisnik.prezime}</h3>
          <div className="text-muted">{korisnik.email}</div>
        </div>
      </div>

      {/* Rezervacije */}
      <div className="card shadow-sm border-0 mb-4">
        <div className="card-body">
          <h5 className="fw-bold mb-3">Moje rezervacije</h5>

          {rezervacije.length === 0 ? (
            <div className="alert alert-light m-0">Nemate još niti jednu rezervaciju.</div>
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
                    <th>Noćenja</th>
                    <th>Ukupna cijena</th>
                  </tr>
                </thead>
                <tbody>
                  {rezervacije.map((rez) => (
                    <tr key={rez.id}>
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
              <div key={rec.id} className="card mb-3 border-0 shadow-sm">
                <div className="card-body">
                  <div className="d-flex justify-content-between flex-wrap gap-2">
                    <div className="fw-semibold">
                      {/* hotel naziv je potreban na profilu */}
                      {rec.hotel?.naziv ?? 'Hotel'}
                    </div>
                    <div aria-label={`Ocjena ${rec.ocjena} od 5`}>
                      {'★'.repeat(rec.ocjena ?? 0)}{'☆'.repeat(Math.max(0, 5 - (rec.ocjena ?? 0)))}
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
    </div>
  );
};

export default ProfilComponent;
