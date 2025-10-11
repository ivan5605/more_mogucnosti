import React, { useEffect, useState } from 'react'
import { getAllHoteli } from '../services/HotelService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';


const NasiHoteliPage = () => {
  const { loggedIn } = useAuth();
  const uloga = localStorage.getItem("uloga");

  const [hoteli, setHoteli] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const [error, setError] = useState(null);

  const navigator = useNavigate();

  const DEFAULT_SLIKA = 'https://res.cloudinary.com/dcolr4yi2/image/upload/v1758037557/0ae34d64f5299aa8dd6d77e28a51680d_oeyree.png';

  const getHotelSlika = (h) => h?.glavnaSlika?.putanja || DEFAULT_SLIKA;

  const onSlikaErr = (e) => {
    e.currentTarget.src = DEFAULT_SLIKA;
  }

  const handleUredi = (idHotel) => {
    navigator(`/admin/urediHotel/${idHotel}`)
  }

  function detaljiHotel(idHotel) {
    navigator(`/hotel/${idHotel}`);
  }

  useEffect(() => {
    (async () => {
      try {
        const response = await getAllHoteli();
        setHoteli(response.data);
      } catch (error) {
        setError("Greška kod dohvaćanja hotela.");
        console.error('Greška kod dohvaćanja hotela:', error);
      } finally {
        setUcitavanje(false);
      };
    })();
  }, [])

  if (ucitavanje) {
    return <div className='container py-5 mt-5'>Učitavanje...</div>
  }

  return (
    <div>
      {error ? (
        <div className='container mt-5 py-4 alert alert-danger'>{error} Molimo pokušajte kasnije.</div>
      ) : (
        <div id='istaknutiHoteli' className='container py-4'>
          <h1 className='text-center mb-5 fw-bold display-4' style={{ fontFamily: 'Segoe UI, sans-serif' }}>
            Naša ponuda hotela na Jadranu
          </h1>

          <div className='row'>
            {
              hoteli.map((hotel, index) => (
                <div key={hotel.id} className='col-12 mb-5'>
                  <div className='card shadow-lg border-0' style={{ borderRadius: '15px' }}>
                    <div className='row g-0 align-items-stretch'>

                      {index % 2 === 0 ? (
                        <>
                          <div className='col-md-5'>
                            <img
                              src={getHotelSlika(hotel)}
                              onError={onSlikaErr}
                              alt="Slika hotela"
                              className='img-fluid h-100'
                              style={{ objectFit: 'cover', borderTopLeftRadius: '15px', borderBottomLeftRadius: '15px' }}
                            />
                          </div>
                          <div className='col-md-7 d-flex'>
                            <div className='card-body d-flex flex-column h-100 py-4 px-5'>
                              <h3 className='card-title mb-3'>{hotel.naziv}</h3>
                              <p className='card-text text-muted mb-2'><strong>Grad:</strong> {hotel.grad.imeGrad}</p>
                              <p className='card-text text-muted mb-2'><strong>Adresa:</strong> {hotel.adresa}</p>
                              <p className='card-text text-muted mb-2'><strong>Parking:</strong>{' '}
                                {hotel.parking ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <p className='card-text text-muted mb-2'><strong>WiFi:</strong>{' '}
                                {hotel.wifi ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <p className='card-text text-muted mb-5'><strong>Bazen:</strong>{' '}
                                {hotel.bazen ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <div className="mt-auto d-flex justify-content-end gap-2">
                                {loggedIn && uloga === 'ADMIN' && (
                                  <button className="btn btn-outline-primary" onClick={() => handleUredi(hotel.id)}>Uredi</button>
                                )}
                                <button className="btn btn-outline-secondary" onClick={() => detaljiHotel(hotel.id)}>Zanima me</button>
                              </div>
                            </div>
                          </div>
                        </>
                      ) : (
                        <>
                          <div className='col-md-7'>
                            <div className='card-body py-4 px-5'>
                              <h3 className='card-title mb-3'>{hotel.naziv}</h3>
                              <p className='card-text text-muted mb-2'><strong>Grad:</strong> {hotel.grad.imeGrad}</p>
                              <p className='card-text text-muted mb-2'><strong>Adresa:</strong> {hotel.adresa}</p>
                              <p className='card-text text-muted mb-2'><strong>Parking:</strong>{' '}
                                {hotel.parking ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <p className='card-text text-muted mb-2'><strong>WiFi:</strong>{' '}
                                {hotel.wifi ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <p className='card-text text-muted mb-5'><strong>Bazen:</strong>{' '}
                                {hotel.bazen ? (<i className='fas fa-check text-success'></i>) : (<i className='fas fa-times fa-danger'></i>)}
                              </p>
                              <div className='position-absolute start-0 bottom-0 m-3 d-flex gap-2'>
                                {loggedIn && uloga === 'ADMIN' ? (
                                  <button
                                    type='button'
                                    className='btn btn-outline-primary'
                                    onClick={() => handleUredi(hotel.id)}
                                  >
                                    Uredi
                                  </button>
                                ) : (
                                  <></>
                                )}
                                <button
                                  type='button'
                                  className='btn btn-outline-secondary'
                                  onClick={() => detaljiHotel(hotel.id)}
                                >
                                  Zanima me
                                </button>
                              </div>
                            </div>
                          </div>
                          <div className='col-md-5'>
                            <img
                              src={getHotelSlika(hotel)}
                              onError={onSlikaErr}
                              alt="Slika hotela"
                              className='img-fluid h-100'
                              style={{ objectFit: 'cover', borderTopRightRadius: '15px', borderBottomRightRadius: '15px' }}
                            />
                          </div>
                        </>
                      )}

                    </div>
                  </div>
                </div>
              ))
            }
            {loggedIn && uloga === 'ADMIN' ? (
              <div className='row justify-content-center'>
                <div className='col-12 col-md-6 mb-5'>
                  <div className='card shadow-lg border-0' style={{ borderRadius: '15px', minHeight: '260px' }}>
                    <div className='card-body d-flex align-items-center justify-content-center position-relative'>
                      <button
                        type='button'
                        className='btn p-0 border-0 bg-transparent stretched-link'
                        style={{ cursor: 'pointer' }}
                        aria-label='Dodaj hotel'
                      >
                        <i className='bi bi-plus-square' style={{ fontSize: '4rem' }}></i>
                        <p>Dodaj novi hotel</p>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <></>
            )}
          </div>
        </div>
      )}

    </div>
  )
}

export default NasiHoteliPage