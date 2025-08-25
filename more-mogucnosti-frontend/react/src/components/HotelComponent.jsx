import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getHotel } from '../services/HotelService';
import { getSobeHotela } from '../services/SobaService';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

const HotelComponent = () => {

  const { idHotel } = useParams();

  const navigator = useNavigate();

  const [sobe, setSobe] = useState([]);

  const [hotel, setHotel] = useState({
    naziv: '',
    grad: '',
    adresa: '',
    parking: '',
    wifi: '',
    bazen: '',
    slike: []
  });

  useEffect(() => {
    getHotel(idHotel).then((response) => {

      const glavna = response.data.glavnaSlika?.putanja ? [{
        putanja: response.data.glavnaSlika.putanja
      }] : [];

      const sporedne = Array.isArray(response.data.sporedneSlike)
        ? response.data.sporedneSlike : [];

      setHotel({
        naziv: response.data.naziv,
        grad: response.data.grad.imeGrad,
        adresa: response.data.adresa,
        parking: response.data.parking,
        wifi: response.data.wifi,
        bazen: response.data.bazen,
        slike: [...glavna, ...sporedne]
      })
    }).catch(error => {
      console.error(error);
    })
  }, [idHotel])

  function sobeHotela(id) {
    getSobeHotela(id).then((response) => {
      setSobe(response.data);

    }).catch(error => {
      console.error("Greška kod dohvaćanja soba hotela!", error)
    })
  }

  //console.log() se ispisuje dva put jer sam u development načinu rada s React Strict Mode (pogledaj main.jsx)
  //React namjerno dvaput poziva useEffect (i druge efekte) da detektira neželjene pojave, to se NE događa u produkciji

  //to radi da vidi jel bi mi aplikacija preživela remount - ponovno montiranje komponent
  //React uništi (unmount) komponentu, te ponovno kreira (mount) istu komponentu

  useEffect(() => {
    console.log(hotel);
  }, [hotel])

  useEffect(() => {
    sobeHotela(idHotel);
  }, [idHotel])

  useEffect(() => {
    console.log("Sobe su ažurirane:", JSON.stringify(sobe, null, 2));
  }, [sobe]);

  return (
    <div className='container py-5 mt-5'>
      <h2 className='text-center mb-4 fw-bold display-4' style={{ fontFamily: 'Segoe UI, sans-serif' }}>
        {hotel.naziv}
      </h2>

      <div className='card shadow-lg border-0 mb-4 ' style={{ borderRadius: '15px', overflow: 'hidden' }}>
        {/* u početnom stanju je URL prazan, kao useState i onda baca error, pazi na to! */}
        {/* dok ne stigne odgovor iz API-ja, hotel.glavnaSlikaPutanja je prazna */}
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

      <div className='row g-4'>
        {sobe.map((soba, index) => (
          <div key={index} className='col-md-4'>
            <div className='card h-100 shadow-lg border-0' style={{ borderRadius: '15px', overflow: 'hidden' }}>
              {/* Glavna slika */}
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

              {/* Sadržaj kartice */}
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

                {/* Sporedne slike */}
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

                {/* Gumb Rezerviraj */}
                <button
                  className="btn btn-outline-secondary mt-auto"
                  onClick={() => navigator(`/rezervacija/${soba.idSoba}`)}
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

export default HotelComponent