import React from 'react'
import pozadina from '../assets/pocetna3.jpg';
import './PocetnaComponent.css';

const PocetnaComponent = () => {

  const handleClick = () => {
    const element = document.getElementById("istaknutiHoteli");
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
  };


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
      <div id='istaknutiHoteli' className='mt-5'>

      </div>
    </div>
  )
}

export default PocetnaComponent