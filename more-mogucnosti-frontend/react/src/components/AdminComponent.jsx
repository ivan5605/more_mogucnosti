import React, { useEffect, useState } from 'react';
import { getKorisniciWithCount } from '../services/KorisnikService';
import { useNavigate } from 'react-router-dom';

const AdminComponent = () => {
  const [korisnici, setKorisnici] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  const navigator = useNavigate();

  useEffect(() => {
    getKorisniciWithCount()
      .then((response) => {
        setKorisnici(response.data || []);
        console.log('Uspješno dohvaćanje!');
      })
      .catch((error) => {
        console.error('Greška kod dohvacanja korisnika!', error);
        setErr('Greška kod dohvaćanja korisnika.');
      })
      .finally(() => setLoading(false));
  }, []);

  const inicijali = (ime = '', prezime = '') =>
    `${(ime[0] || '').toUpperCase()}${(prezime[0] || '').toUpperCase()}`;

  return (
    <div className="container py-5 mt-5">
      <div className="card shadow-sm border-0">
        <div className="card-body">
          <h5 className="fw-bold mb-3">Korisnički podaci</h5>

          {loading && (
            <div className="d-flex align-items-center gap-2 text-muted">
              <div className="spinner-border spinner-border-sm" role="status" />
              <span>Učitavanje korisnika…</span>
            </div>
          )}

          {!loading && err && <div className="alert alert-danger m-0">{err}</div>}

          {!loading && !err && korisnici.length === 0 && (
            <div className="alert alert-light m-0">Nema korisnika za prikaz!</div>
          )}

          {!loading && !err && korisnici.length > 0 && (
            <>
              <div className="mb-3 text-muted small">
                Ukupno: <strong>{korisnici.length}</strong>
              </div>

              <div className="row g-3">
                {korisnici.map((k) => {
                  const rez = k.brojRezervacija;
                  const rec = k.brojRecenzija;

                  return (
                    <div key={k.id} className="col-12 col-md-6 col-xl-6">
                      <div className="card h-100 border-0 shadow-sm">
                        <div className="card-body">
                          <div className="d-flex align-items-start gap-3">
                            <div
                              className="rounded-circle d-flex align-items-center justify-content-center bg-primary-subtle border border-primary-subtle"
                              style={{ width: 44, height: 44, flex: '0 0 44px', fontWeight: 700 }}
                              aria-hidden="true"

                            >
                              {inicijali(k.ime, k.prezime)}
                            </div>

                            <div className="flex-grow-1">
                              <div className="d-flex justify-content-between align-items-start">
                                <div>
                                  <div className="fw-semibold">
                                    {(k.prezime) + (', ') + (k.ime)}
                                  </div>
                                  {k.email && (
                                    <a
                                      href={`mailto:${k.email}`}
                                      className="text-decoration-none text-muted small"
                                    >
                                      {k.email}
                                    </a>
                                  )}
                                </div>

                                <div className="text-end">
                                  <span className="badge text-bg-secondary me-1">
                                    Rezervacije: {rez}
                                  </span>
                                  <span className="badge text-bg-secondary">
                                    Recenzije: {rec}
                                  </span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div className="card-footer bg-transparent border-0 pt-0">
                          <button
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => navigator(`/korisnik/detalji/${k.id}`)}
                          >
                            Detalji &gt;&gt;
                          </button>
                        </div>

                      </div>
                    </div>
                  );
                })}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminComponent;
