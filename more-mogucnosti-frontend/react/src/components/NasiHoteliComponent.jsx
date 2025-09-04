import React, { use, useEffect, useState } from 'react'
import { getAllHoteli } from '../services/HotelService';
import { useNavigate } from 'react-router-dom';


const NasiHoteliComponent = () => {

  const [hoteli, setHoteli] = useState([]);

  const [ucitavanje, setUcitavanje] = useState(true);

  const navigator = useNavigate();

  function allHoteli() {
    getAllHoteli().then(response => {
      setHoteli(response.data)
    }).catch(error => {
      console.error("Greška kod dohvaćanja hotela!", error)
    })
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
        console.error('Greška kod dohvaćanja hotela:', error)
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
      <div id='istaknutiHoteli' className='container mt-5 py-5'>
        <h1 className='text-center mb-5 fw-bold display-4' style={{ fontFamily: 'Segoe UI, sans-serif' }}>
          Naša ponuda hotela na Jadranu
        </h1>

        <div className='row'>
          {
            hoteli.map((hotel, index) => (
              <div key={hotel.id} className='col-12 mb-5'>
                <div className='card shadow-lg border-0' style={{ borderRadius: '15px' }}>
                  <div className='row g-0 align-items-stretch'>

                    {/* Image left or right */}
                    {index % 2 === 0 ? (
                      <>
                        <div className='col-md-5'>
                          <img
                            src={hotel.glavnaSlika.putanja}
                            alt="Slika hotela"
                            className='img-fluid h-100'
                            style={{ objectFit: 'cover', borderTopLeftRadius: '15px', borderBottomLeftRadius: '15px' }}
                          />
                        </div>
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
                            <button
                              type='button'
                              className='btn btn-outline-secondary bottom-0 end-0 position-absolute m-3'
                              onClick={() => detaljiHotel(hotel.id)}
                            >
                              Zanima me
                            </button>

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
                            <button
                              type='button'
                              className='btn btn-outline-secondary bottom-0 start-0 position-absolute mb-3 ms-3'
                              onClick={() => detaljiHotel(hotel.id)}
                            >
                              Zanima me
                            </button>
                          </div>
                        </div>
                        <div className='col-md-5'>
                          <img
                            src={hotel.glavnaSlika.putanja}
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
        </div>
      </div>
    </div>
  )
}

export default NasiHoteliComponent