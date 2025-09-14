import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { upsertRecenzija } from '../services/RecenzijaService';
import { toast } from 'react-toastify';

const RecenzijaComponent = ({ onSaved, onCancel }) => {
  const { idHotel } = useParams();

  const [recenzija, setRecenzija] = useState({
    ocjena: '',
    tekst: ''
  });

  const [errors, setErrors] = useState({
    ocjena: '',
    tekst: ''
  });

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

  function provjeriUnos() {
    const errorsCopy = {
      ocjena: provjeriOcjena(recenzija.ocjena),
      tekst: provjeriTekst(recenzija.tekst)
    };
    setErrors(errorsCopy);
    return Object.values(errorsCopy).every(err => err === '');
  }

  function spremiRecenzija(e) {
    e.preventDefault();
    if (!provjeriUnos()) return;

    upsertRecenzija(idHotel, recenzija)
      .then(() => {
        toast.success('Recenzija spremljena!', { autoClose: 2000, position: 'bottom-left' });
        // očisti formu
        setRecenzija({ ocjena: '', tekst: '' });
        setErrors({ ocjena: '', tekst: '' });
        // zatvori formu
        if (onSaved) onSaved();
      })
      .catch((error) => {
        console.error('Greška kod spremanja recenzije!', error);
        toast.error('Greška kod spremanja recenzije.', { autoClose: 2500, position: 'bottom-left' });
      });
  }

  const Star = ({ filled }) => (
    <i className={`${filled ? 'fas' : 'far'} fa-star ${filled ? 'text-warning' : 'text-muted'}`} aria-hidden="true"></i>
  );

  return (
    <section
      className='py-4'
      style={{ background: 'linear-gradient(135deg,#f8f9fa 0%, #eef2f7 100%)', borderRadius: '16px' }}
    >
      <div className='container'>
        <div className='row d-flex justify-content-center'>
          <div className='col-lg-10 col-xl-8'>
            <div className='card border-0 shadow' style={{ borderRadius: '16px' }}>
              <div className='card-body p-4 p-md-5'>
                <div className='text-center mb-4'>
                  <div className='d-inline-flex align-items-center gap-2'>
                    <i className='fas fa-pen-alt fa-lg text-primary'></i>
                    <h1 className='h3 fw-bold m-0'>Napišite recenziju</h1>
                  </div>
                  <p className='text-muted mt-2 mb-0'>Ocijenite smještaj i podijelite svoje iskustvo.</p>
                </div>

                <form className='mx-1 mx-md-2'>

                  {/* Ocjena */}
                  <div className='mb-4'>
                    <label className='form-label fw-semibold d-flex align-items-center'>
                      <i className='fas fa-star me-2 text-warning'></i> Ocjena <span className='text-danger ms-1'>*</span>
                    </label>

                    <div role='radiogroup' aria-label='Ocjena' className='mb-2'>
                      {[1, 2, 3, 4, 5].map((n) => (
                        <div className='form-check form-check-inline' key={n}>
                          <input
                            className='form-check-input'
                            type='radio'
                            name='ocjena'
                            value={n}
                            checked={String(recenzija.ocjena) === String(n)}
                            onChange={(e) => {
                              setRecenzija((prev) => ({ ...prev, ocjena: e.target.value }));
                              if (errors.ocjena) setErrors((prev) => ({ ...prev, ocjena: '' }));
                            }}
                          />
                          <label className='form-check-label' htmlFor={`ocjena-${n}`}>
                            {n}
                          </label>
                        </div>
                      ))}
                    </div>

                    <div
                      className={`form-control ${errors.ocjena ? 'is-invalid' : ''} bg-white`}
                      style={{ height: 'auto', padding: '0.6rem 0.75rem', borderRadius: '12px' }}
                      aria-hidden='true'
                    >
                      {[1, 2, 3, 4, 5].map((n) => (
                        <span key={`star-${n}`} className='me-1 fs-5'>
                          <Star filled={Number(recenzija.ocjena) >= n} />
                        </span>
                      ))}
                    </div>
                    {errors.ocjena && <div className='invalid-feedback d-block'>{errors.ocjena}</div>}
                  </div>

                  {/* Tekst */}
                  <div className='mb-4'>
                    <label className='form-label fw-semibold' htmlFor='tekst'>
                      Tekst recenzije <span className='text-danger'>*</span>
                    </label>
                    <textarea
                      id='tekst'
                      className={`form-control ${errors.tekst ? 'is-invalid' : ''}`}
                      placeholder='Što mislite o ovom hotelu?'
                      rows={6}
                      name='tekst'
                      maxLength={1000}
                      value={recenzija.tekst}
                      onChange={(e) => {
                        setRecenzija({ ...recenzija, tekst: e.target.value });
                        if (errors.tekst) setErrors((prev) => ({ ...prev, tekst: '' }));
                      }}
                      onBlur={() => setErrors((prev) => ({ ...prev, tekst: provjeriTekst(recenzija.tekst) }))}
                      style={{ borderRadius: '12px' }}
                    />
                    <div className='form-text text-end'>{recenzija.tekst.length}/1000</div>
                    {errors.tekst && <div className='invalid-feedback'>{errors.tekst}</div>}
                  </div>

                  <div className='d-flex justify-content-center gap-3'>
                    <button
                      type='button'
                      className='btn btn-primary btn-lg px-4'
                      onClick={spremiRecenzija}
                    >
                      Spremi
                    </button>
                    <button
                      type='button'
                      className='btn btn-outline-secondary btn-lg px-4'
                      onClick={() => {
                        // očisti i zatvori
                        setRecenzija({ ocjena: '', tekst: '' });
                        setErrors({ ocjena: '', tekst: '' });
                        if (onCancel) onCancel();
                      }}
                    >
                      Odustani
                    </button>
                  </div>

                  <p className='text-muted small text-center mt-3 mb-0'>
                    Polja označena <span className='text-danger'>*</span> su obavezna.
                  </p>

                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default RecenzijaComponent;
