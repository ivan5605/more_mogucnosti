import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getHotel } from '../services/HotelService';
import { getSobeHotela } from '../services/SobaService';
import { getRecenzijeHotela, getInfoRecenzija } from '../services/RecenzijaService';
import { useAuth } from '../auth/AuthContext';
import { toast } from 'react-toastify';
import RecenzijaComponent from '../components/RecenzijaComponent';

const HotelPage = () => {

  const { loggedIn, logout } = useAuth();

  const { idHotel } = useParams();

  const navigator = useNavigate();

  const [sobe, setSobe] = useState([]);

  const [prikaziSve, setPrikaziSve] = useState(false);

  const [hotel, setHotel] = useState({
    naziv: '',
    grad: '',
    adresa: '',
    parking: '',
    wifi: '',
    bazen: '',
    slike: []
  });

  const [recenzija, setRecenzija] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const [prikaziFormaRecenzije, setPrikaziFormaRecenzije] = useState(false);

  const [infoRec, setInfoRec] = useState({
    broj: 0,
    prosjek: 0
  });

  useEffect(() => {
    (async () => {
      setUcitavanje(true);

      const results = await Promise.allSettled([
        getHotel(idHotel),
        getRecenzijeHotela(idHotel),
        getSobeHotela(idHotel),
        getInfoRecenzija(idHotel),
      ]);

      const [hotelRes, recRes, sobeRes, infoRes] = results;

      //hotel
      if (hotelRes.status === 'fulfilled') {
        const h = hotelRes.value.data;
        const glavna = h.glavnaSlika?.putanja ? [{ putanja: h.glavnaSlika.putanja }] : [];
        const sporedne = Array.isArray(h.sporedneSlike) ? h.sporedneSlike : [];
        setHotel({
          naziv: h.naziv,
          grad: h.grad?.imeGrad ?? '',
          adresa: h.adresa ?? '',
          parking: !!h.parking,
          wifi: !!h.wifi,
          bazen: !!h.bazen,
          slike: [...glavna, ...sporedne]
        });
      } else {
        console.error('Greška kod hotela:', hotelRes.reason);
      }

      //recenzije
      if (recRes.status === 'fulfilled') {
        const arr = Array.isArray(recRes.value.data) ? [...recRes.value.data] : [];
        arr.sort((a, b) => new Date(b.datum) - new Date(a.datum));
        setRecenzija(arr);
      } else {
        console.warn('Nema recenzija ili greška:', recRes.reason);
        setRecenzija([]); // fallback
      }

      //sobe
      if (sobeRes.status === 'fulfilled') {
        setSobe(sobeRes.value.data ?? []);
      } else {
        console.error('Greška kod soba:', sobeRes.reason);
        setSobe([]); // fallback
      }

      //info recenzije
      if (infoRes.status === 'fulfilled') {
        const d = infoRes.value.data || {};
        setInfoRec({
          broj: d.brojRecenzija ?? 0,
          prosjek: d.prosjekRecenzija ?? 0,
        });
      } else {
        console.warn('Info recenzija API 500/404 → postavljam 0/0');
        setInfoRec({ broj: 0, prosjek: 0 }); // fallback
      }

      setUcitavanje(false);
    })();
  }, [idHotel]);

  if (ucitavanje) {
    return <div className='container py-5 mt-5'>Učitavanje...</div>
  }

  function handleRezerviraj(idSoba) {
    {
      !loggedIn ? (
        toast.error("Za rezervaciju je potrebna prijava!", {
          autoClose: 4000,
          position: 'bottom-left'
        })
      ) : (
        navigator(`/rezervacija/${idSoba}`)
      )
    }
  }

  function handleRecenzija(hotelId) {
    if (!loggedIn) {
      toast.error("Za dodavanje recenzije je potrebna prijava!", {
        autoClose: 4000,
        position: 'bottom-left'
      });
    } else {
      setPrikaziFormaRecenzije(true);
    }
  }

  const refreshRecenzije = async () => {
    try {
      const res = await getRecenzijeHotela(idHotel);
      const arr = Array.isArray(res.data) ? [...res.data] : [];
      arr.sort((a, b) => new Date(b.datum) - new Date(a.datum));
      setRecenzija(arr);
    } catch (e) {
      console.error('Greška kod osvježavanja recenzija:', e);
    }
  };

  const refreshInfoRec = async () => {
    try {
      const infoRes = await getInfoRecenzija(idHotel);
      setInfoRec({
        broj: infoRes.data.brojRecenzija,
        prosjek: infoRes.data.prosjekRecenzija
      });
    } catch (e) {
      console.error('Greška kod osvježavanja info recenzija:', e);
    }
  };

  const handleRecSaved = async () => {
    await Promise.all([refreshRecenzije(), refreshInfoRec()]);
    setPrikaziFormaRecenzije(false);
  };

  const renderZv = (avg) => {
    const prosjek = Math.round(avg || 0);
    return '★'.repeat(prosjek) + '☆'.repeat(5 - prosjek);
  };

  return (
    <div className='container py-4'>
      <h2 className='text-center mb-2 fw-bold display-4' style={{ fontFamily: 'Segoe UI, sans-serif' }}>
        {hotel.naziv}
      </h2>

      <div className="text-center mb-4">
        {infoRec.broj > 0 ? (
          <div className="d-inline-flex align-items-center gap-3">
            <span className="fs-5" aria-label={`Prosjek ${infoRec.prosjek?.toFixed?.(2) ?? '0.00'} od 5`}>
              {renderZv(infoRec.prosjek)}
            </span>
            <span className="text-muted">
              {Number(infoRec.prosjek).toFixed(2)} / 5 · {infoRec.broj} recenzij{infoRec.broj === 1 ? 'a' : (infoRec.broj >= 2 && infoRec.broj <= 4 ? 'e' : 'a')}
            </span>
          </div>
        ) : (
          <></>
        )}
      </div>

      <div className='card shadow-lg border-0 mb-4 ' style={{ borderRadius: '15px', overflow: 'hidden' }}>
        {Array.isArray(hotel.slike) && hotel.slike.length > 0 && (
          <div className="d-flex justify-content-center">
            <div
              id="hotelCarousel"
              className="carousel slide mb-5"
              data-bs-ride="carousel"
              style={{ width: '100%' }}
            >
              <div className="carousel-inner">
                {hotel.slike.map((slika, index) => (
                  <div
                    className={`carousel-item ${index === 0 ? 'active' : ''}`}
                    key={index}
                  >
                    <img
                      src={slika.putanja}
                      className="d-block w-100"
                      alt={`Sporedna slika ${index + 1}`}
                      style={{ height: '500px', objectFit: 'cover', borderRadius: '10px' }}
                    />
                  </div>
                ))}
              </div>

              <button
                className="carousel-control-prev"
                type="button"
                data-bs-target="#hotelCarousel"
                data-bs-slide="prev"
              >
                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                <span className="visually-hidden">Previous</span>
              </button>
              <button
                className="carousel-control-next"
                type="button"
                data-bs-target="#hotelCarousel"
                data-bs-slide="next"
              >
                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                <span className="visually-hidden">Next</span>
              </button>
            </div>
          </div>
        )}
        <div className='row text-center fs-5 fw-medium mb-4 mt-4'>
          <div className='col'>
            <p className='text-muted m-0'><strong>Grad:</strong> {hotel.grad}</p>
          </div>

          <div className='col'>
            <p className='text-muted m-0'><strong>Adresa:</strong> {hotel.adresa}</p>
          </div>

          <div className='col'>
            <p className='text-muted m-0'><strong>Parking:</strong>{' '}
              {hotel.parking ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
            </p>
          </div>

          <div className='col'>
            <p className='text-muted m-0'><strong>WiFi:</strong>{' '}
              {hotel.wifi ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
            </p>
          </div>

          <div className='col'>
            <p className='text-muted m-0'><strong>Bazen:</strong>{' '}
              {hotel.bazen ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
            </p>
          </div>
        </div>
      </div>

      <div className="mt-4 mb-4">
        <h4 className="fw-bold mb-3 text-center">Recenzije</h4>

        {recenzija.length === 0 ? (
          <div className="text-center">
            <div className="text-muted mb-2">Još nema recenzija za ovaj hotel.</div>
            {!prikaziFormaRecenzije && (
              <button
                className="btn btn-primary"
                onClick={() => handleRecenzija(idHotel)}
              >
                Dodaj recenziju
              </button>
            )}
          </div>
        ) : (
          <>
            {(prikaziSve ? recenzija : recenzija.slice(0, 3)).map((rec) => (
              <div key={rec.id} className="card mb-3 shadow-sm border-0">
                <div key={rec.id} className="card mb-3 shadow-sm border-0">
                  <div className="card-body">
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="fw-semibold"> {rec.korisnik?.ime} {rec.korisnik?.prezime}
                      </div>
                      <div aria-label={`Ocjena ${rec.ocjena} od 5`}> {'★'.repeat(rec.ocjena)}{'☆'.repeat(5 - rec.ocjena)}
                      </div>
                    </div> {rec.tekst && <p className="mb-2 mt-2">{rec.tekst}</p>}
                    <small className="text-muted"> {new Date(rec.datum).toLocaleDateString('hr-HR')} </small>
                  </div>
                </div>
              </div>
            ))}

            <div className="text-center mt-3">
              {recenzija.length > 3 && (
                !prikaziSve ? (
                  <button
                    className="btn btn-outline-primary mx-1"
                    onClick={() => setPrikaziSve(true)}
                  >
                    Prikaži više
                  </button>
                ) : (
                  <button
                    className="btn btn-outline-secondary mx-1"
                    onClick={() => setPrikaziSve(false)}
                  >
                    Prikaži manje
                  </button>
                )
              )}

              {!prikaziFormaRecenzije && (
                <button
                  className="btn btn-primary mx-1"
                  onClick={() => handleRecenzija(idHotel)}
                >
                  Dodaj recenziju
                </button>
              )}
            </div>
          </>
        )}
      </div>


      {/* Forma za recenziju – korisnik klikne na gumb */}
      {prikaziFormaRecenzije && (
        <div className="mt-4">
          <RecenzijaComponent onSaved={handleRecSaved} onCancel={() => setPrikaziFormaRecenzije(false)} />
        </div>
      )}

      <div className='row g-4'>
        {sobe.map((soba, index) => (
          <div key={index} className='col-md-4'>
            <div className='card h-100 shadow-lg border-0' style={{ borderRadius: '15px', overflow: 'hidden' }}>

              {soba.glavnaSlika && (
                <div className='overflow-hidden' style={{ height: '250px' }}>
                  <img
                    src={soba.glavnaSlika.putanja}
                    alt='Glavna slika sobe'
                    className='card-img-top'
                    style={{
                      height: '100%',
                      width: '100%',
                      objectFit: 'cover',
                      transition: 'transform 0.4s ease'
                    }}
                    onMouseOver={(e) => (e.currentTarget.style.transform = 'scale(1.05)')}
                    onMouseOut={(e) => (e.currentTarget.style.transform = 'scale(1)')}
                  />
                </div>
              )}

              <div className='card-body d-flex flex-column'>
                <h4 className='card-title text-center fw-bold mb-3'>
                  Soba {soba.brojSobe}
                </h4>

                <ul className='list-unstyled mb-4'>
                  <li><strong>Kapacitet:</strong> {soba.kapacitet} osobe</li>
                  <li><strong>Cijena:</strong> {soba.cijenaNocenja} €</li>
                  <li>
                    <strong>Balkon:</strong>{' '}
                    {soba.balkon ? (
                      <i className='fas fa-check text-success ms-1'></i>
                    ) : (
                      <i className='fas fa-times text-danger ms-1'></i>
                    )}
                  </li>
                  <li>
                    <strong>Pet friendly:</strong>{' '}
                    {soba.petFriendly ? (
                      <i className='fas fa-check text-success ms-1'></i>
                    ) : (
                      <i className='fas fa-times text-danger ms-1'></i>
                    )}
                  </li>
                </ul>

                {soba.sporedneSlike?.length > 0 && (
                  <div className='row g-2 mb-3'>
                    {soba.sporedneSlike.map((slika, i) => (
                      <div key={i} className='col-6'>
                        <img
                          src={slika.putanja}
                          alt={`Sporedna slika ${i + 1}`}
                          style={{
                            height: '150px',
                            width: '100%',
                            objectFit: 'cover',
                            borderRadius: '8px'
                          }}
                        />
                      </div>
                    ))}
                  </div>
                )}

                <button
                  className="btn btn-outline-secondary mt-auto"
                  onClick={() => handleRezerviraj(soba.id)}
                >
                  Rezerviraj
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

    </div>
  )
}

export default HotelPage;
