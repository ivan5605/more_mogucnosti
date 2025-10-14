import React, { useState } from 'react';

const PRIMATELJ = 'kontakt@more-mogucnosti.hr'; // promijeni po želji

const KontaktPage = () => {
  const [form, setForm] = useState({ ime: '', email: '', poruka: '' });
  const [err, setErr] = useState({ ime: '', email: '', poruka: '' });

  const validate = () => {
    const e = {
      ime: form.ime.trim() ? '' : 'Unesite ime i prezime.',
      email: form.email.trim()
        ? '' : 'Unesite email.',
      poruka: form.poruka.trim().length >= 5 ? '' : 'Poruka je prekratka (min 5 znakova).'
    };
    setErr(e);
    return Object.values(e).every((x) => x === '');
  };

  const onChange = (ev) => {
    const { name, value } = ev.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    setErr((prev) => ({ ...prev, [name]: '' }));
  };

  const onSubmit = (ev) => {
    ev.preventDefault();
    if (!validate()) return;

    const subject = `Upit s weba – ${form.ime}`;
    const bodyLines = [
      `Ime i prezime: ${form.ime}`,
      `Email pošiljatelja: ${form.email}`,
      '',
      'Poruka:',
      form.poruka
    ];
    const body = bodyLines.join('\n');

    const mailto = `mailto:${encodeURIComponent(PRIMATELJ)}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

    window.location.href = mailto;

    setForm({ ime: '', email: '', poruka: '' });
  };

  return (
    <div className="container py-5">
      <div className="card border-0 shadow-sm mb-4" style={{ borderRadius: 12 }}>
        <div className="card-body p-4">
          <div className="d-flex align-items-center gap-3">
            <i className="bi bi-envelope-paper fs-3 text-primary"></i>
            <h2 className="m-0">Kontakt</h2>
          </div>
          <p className="text-muted mb-0 mt-2">
            Ispuni formu — i pošalji email!
          </p>
        </div>
      </div>

      <div className="row g-4">
        <div className="col-12 col-lg-6">
          <div className="card border-0 shadow-sm" style={{ borderRadius: 12 }}>
            <div className="card-body p-4">
              <h5 className="fw-bold mb-3">Pošalji poruku</h5>
              <form onSubmit={onSubmit} noValidate>
                <div className="mb-3">
                  <label className="form-label">Ime i prezime</label>
                  <input
                    type="text"
                    className={`form-control ${err.ime ? 'is-invalid' : ''}`}
                    placeholder="Vaše ime i prezime"
                    name="ime"
                    value={form.ime}
                    onChange={onChange}
                  />
                  {err.ime && <div className="invalid-feedback">{err.ime}</div>}
                </div>

                <div className="mb-3">
                  <label className="form-label">Vaš email</label>
                  <input
                    type="email"
                    className={`form-control ${err.email ? 'is-invalid' : ''}`}
                    placeholder="primjer@domena.com"
                    name="email"
                    value={form.email}
                    onChange={onChange}
                  />
                  {err.email && <div className="invalid-feedback">{err.email}</div>}
                </div>

                <div className="mb-3">
                  <label className="form-label">Poruka</label>
                  <textarea
                    className={`form-control ${err.poruka ? 'is-invalid' : ''}`}
                    rows={6}
                    placeholder="Kako vam možemo pomoći?"
                    name="poruka"
                    value={form.poruka}
                    onChange={onChange}
                    maxLength={1000}
                  />
                  <div className="form-text text-end">{form.poruka.length}/1000</div>
                  {err.poruka && <div className="invalid-feedback d-block">{err.poruka}</div>}
                </div>

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary">Otvori email</button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-6">
          <div className="card h-100 border-0 shadow-sm" style={{ borderRadius: 12 }}>
            <div className="card-body p-4">
              <h5 className="fw-bold mb-3">Podaci</h5>
              <ul className="list-unstyled mb-0">
                <li className="mb-3">
                  <i className="bi bi-geo-alt me-2 text-primary"></i>
                  Ulica Matije Gupca 64, 10000 Zagreb
                </li>
                <li className="mb-3">
                  <i className="bi bi-telephone me-2 text-primary"></i>
                  +385 91 396 3630
                </li>
                <li className="mb-0">
                  <i className="bi bi-envelope me-2 text-primary"></i>
                  {PRIMATELJ}
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default KontaktPage;
