import React, { useEffect, useState } from 'react';
import pozadina from '../assets/pocetna3.jpg';
import './PocetnaComponent.css';
import { getRandomHoteli } from '../services/HotelService';
import { useNavigate } from 'react-router-dom';
import { getExpAt } from '../services/AuthService';

const PocetnaComponent = () => {
  const navigate = useNavigate();

  function detaljiHotel(idHotel) {
    navigate(`/hotel/${idHotel}`);
  }

  const goPonuda = () => navigate('/nasiHoteli');

  const handleScrollIstaknuti = () => {
    const el = document.getElementById('istaknutiHoteli');
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  };

  const [hoteli, setHoteli] = useState([]);
  const [ucitavanje, setUcitavanje] = useState(true);
  const [greska, setGreska] = useState(null);

  function allHoteli() {
    setGreska(null);
    setUcitavanje(true);
    getRandomHoteli()
      .then((response) => {
        const arr = Array.isArray(response.data) ? response.data : [];
        setHoteli(arr);
      })
      .catch((error) => {
        console.error('Greška kod dohvaćanja hotela!', error);
        setGreska('Ne možemo učitati istaknute hotele.');
      })
      .finally(() => setUcitavanje(false));
  }

  useEffect(() => {
    allHoteli();
  }, []);

  if (ucitavanje) {
    return <div className='container py-5 mt-5'>Učitavanje...</div>
  }

  return (
    <div>
      <div className="pocetnaBaza" style={{ backgroundImage: `url(${pozadina})` }}>
        <div className="pocetnaOverlay"></div>
        <div className="pocetnaSadrzaj container">
          <h1 className="fw-bold display-4 mb-3">Otkrijte svoj idealni odmor na Jadranu!</h1>
          <p className="lead mb-4 fst-italic">Rezerviraj hotel brzo, jednostavno i sigurno.</p>

          <div className="d-flex gap-3 justify-content-center flex-wrap">
            <button className="btn btn-primary btn-lg" onClick={goPonuda}>
              Pogledaj ponudu hotela
            </button>
            <button className="btn btn-outline-light btn-lg" onClick={handleScrollIstaknuti}>
              Istaknuti hoteli
            </button>
          </div>
        </div>
      </div>

      <div id="istaknutiHoteli" className="container my-5">
        <div className="d-flex align-items-center justify-content-between mb-3">
          <h2 className="m-0">Istaknuti hoteli</h2>
          <button className="btn btn-link text-decoration-none" onClick={goPonuda}>
            Pogledaj sve &rarr;
          </button>
        </div>

        {greska && <div className="alert alert-danger">{greska}</div>}

        {ucitavanje ? (
          <div className="row g-4">
            {Array.from({ length: 6 }).map((_, i) => (
              <div className="col-12 col-sm-6 col-lg-4" key={i}>
                <div className="card border-0 shadow-sm placeholder-card">
                  <div className="placeholder-img w-100"></div>
                  <div className="card-body">
                    <div className="placeholder-glow">
                      <span className="placeholder col-7"></span>
                      <span className="placeholder col-5"></span>
                      <span className="placeholder col-4"></span>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : hoteli.length === 0 ? (
          <div className="alert alert-light">Trenutno nema istaknutih hotela.</div>
        ) : (
          <div className="row g-4">
            {hoteli.map((hotel) => (
              <div className="col-12 col-sm-6 col-lg-4" key={hotel.id}>
                <div className="card h-100 border-0 shadow-sm">
                  <div className="ratio ratio-16x9">
                    <img
                      src={hotel.glavnaSlika?.putanja}
                      alt="Slika hotela"
                      className="w-100 h-100"
                      style={{ objectFit: 'cover', borderTopLeftRadius: '0.5rem', borderTopRightRadius: '0.5rem' }}
                    />
                  </div>
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title mb-1">{hotel.naziv}</h5>
                    <div className="text-muted mb-3">{hotel.grad?.imeGrad} • {hotel.adresa}</div>

                    <div className="d-flex flex-wrap gap-2 mb-4">
                      <span className={`badge ${hotel.wifi ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}`}>WiFi</span>
                      <span className={`badge ${hotel.parking ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}`}>Parking</span>
                      <span className={`badge ${hotel.bazen ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}`}>Bazen</span>
                    </div>

                    <div className="mt-auto d-flex gap-2">
                      <button type="button" className="btn btn-outline-secondary w-100" onClick={() => detaljiHotel(hotel.id)}>
                        Detalji
                      </button>

                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PocetnaComponent;
