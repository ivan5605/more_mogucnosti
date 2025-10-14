import React from 'react';

const OnamaPage = () => {
  return (
    <div className="container py-5">
      <div className="card border-0 shadow-sm mb-4" style={{ borderRadius: 12 }}>
        <div className="card-body p-4 p-md-5">
          <div className="d-flex align-items-center gap-3 mb-2">
            <i className="bi bi-building fs-3 text-primary"></i>
            <h2 className="m-0">O nama</h2>
          </div>
          <p className="text-muted mb-0">
            More Mogućnosti je mala, fokusirana ekipa koja voli raditi jasne, jednostavne i brze web stvari.
            Naši hoteli i partneri dobivaju točno ono što im treba — bez suvišnih komplikacija.
          </p>
        </div>
      </div>

      <div className="card border-0 shadow-sm" style={{ borderRadius: 12 }}>
        <div className="card-body p-4">
          <h5 className="fw-bold mb-3">Naše vrijednosti</h5>
          <ul className="list-unstyled mb-4">
            <li className="mb-2">
              <i className="bi bi-check2 text-success me-2"></i>
              Fokus na iskustvo gosta i vlasnika.
            </li>
            <li className="mb-2">
              <i className="bi bi-check2 text-success me-2"></i>
              Performanse ispred “wow” efekata.
            </li>
            <li className="mb-2">
              <i className="bi bi-check2 text-success me-2"></i>
              Kontinuirano poboljšavanje malim koracima.
            </li>
          </ul>

          <h6 className="text-muted">Gdje radimo?</h6>
          <p className="mb-0">
            Remote-first, bazirani u Hrvatskoj. Radimo s turističkim subjektima diljem regije.
          </p>
        </div>
      </div>
    </div>
  );
};

export default OnamaPage;
