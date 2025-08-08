import React, { useEffect, useState } from 'react'
import pozadina from '../assets/pocetna3.jpg';
import './PocetnaComponent.css';
import { getAllHoteli, getRandomHoteli } from '../services/HotelService';
import slika from '../assets/pocetna2.jpg';
import { useNavigate } from 'react-router-dom';

const PocetnaComponent = () => {

  const navigator = useNavigate();

  function detaljiHotel(idHotel) {
    navigator(`/hotel/${idHotel}`);
  }

  const handleClick = () => {
    const element = document.getElementById("istaknutiHoteli");
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
  };

  const [hoteli, setHoteli] = useState([]);


  function allHoteli() {
    getRandomHoteli().then(response => {
      setHoteli(response.data)
    }).catch(error => {
      console.error("Greška kod dohvaćanja hotela!", error)
    })
  }

  useEffect(() => {
    allHoteli();
  }, [])

  return (
    <div>
      <div className='pocetnaBaza' style={{ backgroundImage: `url(${pozadina})` }}>
        <div className='pocetnaOverlay'></div>
        <div className='pocetnaSadrzaj'>
          <h1 className='fw-bold display-4'>Otkrij svoj idealni odmor na Jadranu!</h1>
          <h2 className='fw-light fst-italic lead mb-4'>Rezerviraj hotel brzo, jednostavno i sigurno.</h2>
          <button className="btn btn-secondary btn-lg" onClick={handleClick}>
            Prikaži istaknuto
          </button>
        </div>
      </div>

      <div id='istaknutiHoteli' className='container mt-5'>
        <h1 className='text-center mb-5 fw-bold display-4' style={{ fontFamily: 'Segoe UI, sans-serif' }}>
          Favoriti ovog mjeseca
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
                            <p className='card-text text-muted mb-2'><strong>Grad:</strong> {hotel.grad}</p>
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
                            <button type='button' className='btn btn-outline-secondary bottom-0 end-0 position-absolute m-3' onClick={() => detaljiHotel(hotel.id)}>Zanima me</button>
                          </div>

                        </div>
                      </>
                    ) : (
                      <>
                        <div className='col-md-7'>
                          <div className='card-body py-4 px-5'>
                            <h3 className='card-title mb-3'>{hotel.naziv}</h3>
                            <p className='card-text text-muted mb-2'><strong>Grad:</strong> {hotel.grad}</p>
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
                            <button type='button' className='btn btn-outline-secondary bottom-0 start-0 position-absolute m-3' onClick={() => detaljiHotel(hotel.id)}>Zanima me</button>

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

export default PocetnaComponent