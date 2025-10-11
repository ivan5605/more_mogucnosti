import React, { useEffect, useMemo, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'

import { getHotel, updateHotel, softDeleteHotel, aktivirajHotel } from '../services/HotelService';
import { getSobeHotela, addSoba, updateSoba, softDeleteSoba, aktivirajSoba, getSoba } from '../services/SobaService';
import { setGlavnaH, deleteSlikaH, addSlikaH } from '../services/SlikaHotelService';
import { setGlavnaS, deleteSlikaS, addSlikaS } from '../services/SlikaSobaService';
import { getRezervacijaHotela, adminDeleteRezervacija, adminUpdateRezervacija, getZauzetiDatumi } from '../services/RezervacijaService';

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const UrediHotel = () => {

  const { idHotel } = useParams();
  const navigator = useNavigate();

  const [hotel, setHotel] = useState({
    naziv: '',
    grad: '',
    adresa: '',
    parking: false,
    wifi: false,
    bazen: false,
    glavnaSlika: null,
    sporedneSlike: [],
    aktivan: false
  });

  const [sobe, setSobe] = useState([]);
  const [rezervacije, setRezervacije] = useState([]);
  const [soba, setSoba] = useState(null);

  const [ucitavanje, setUcitavanje] = useState(false);

  const [poruka, setPoruka] = useState(null);

  const [slika, setSlika] = useState({
    putanja: '',
    glavna: false
  });

  const [sobaId, setSobaId] = useState(null);

  const [sobaDto, setSobaDto] = useState({
    id: '',
    brojSobe: '',
    kapacitet: '',
    cijenaNocenja: '',
    balkon: false,
    petFriendly: false
  });

  const [urediSobaId, setUrediSobaId] = useState(null);

  const [modalDodajS, setModalDodajS] = useState(false);
  const [modalUrediS, setModalUrediS] = useState(false);
  const [modalUrediSlikeS, setModalUrediSlikeS] = useState(false);

  const [zauzetiTermini, setZauzetiTermini] = useState([]);
  const [pocetniDatum, setPocetniDatum] = useState(null);
  const [zavrsniDatum, setZavrsniDatum] = useState(null);

  const [odabranaRez, setOdabranaRez] = useState(null);
  const [modalRez, setModalRez] = useState(false);

  const [formaRez, setFormaRez] = useState({
    brojOsoba: 1,
    datumPocetak: '',
    datumKraj: ''
  });

  const [urediRezErr, setUrediRezErr] = useState({
    datumPocetak: '',
    datumKraj: '',
    brojOsoba: ''
  });

  //OPTIONAL CHAINING
  //? ako je lijevo null ili undefined ->  vrati undefined (h.rad?.imeGrad - ako grad ne postoji, rezultat je undefined)

  //NULLISH COALESCING
  //?? ako je lijevo null ili undefined -> vraca zadanu vrijednost
  const mapHotel = (h) => {
    return {
      naziv: h.naziv,
      grad: h.grad.imeGrad,
      adresa: h.adresa,
      parking: !!h.parking,
      wifi: !!h.wifi,
      bazen: !!h.bazen,
      glavnaSlika: h.glavnaSlika ? {
        id: h.glavnaSlika.id,
        putanja: h.glavnaSlika.putanja
      } : null,
      sporedneSlike: Array.isArray(h.sporedneSlike) ? h.sporedneSlike : [],
      aktivan: h.aktivan
    };
  };

  const refreshHotel = async () => {
    const res = await getHotel(idHotel);
    setHotel(mapHotel(res.data));
  };

  const refreshSobe = async () => {
    const res = await getSobeHotela(idHotel);
    setSobe(res.data ?? []);
  };

  const refreshSoba = async () => {
    const res = await getSoba(sobaId);
    setSoba(res.data);
  };

  useEffect(() => {
    (async () => {
      if (!sobaId) {
        setSoba(null);
        return;
      };

      const res = await getSoba(sobaId);
      setSoba(res.data ?? null);
    })();
  }, [sobaId]);

  const slikeSobe = useMemo(() => {
    if (!soba) return [];
    const slike = [];
    if (soba.glavnaSlika) {
      slike.push({ ...soba.glavnaSlika, glavna: true });
    };
    if (Array.isArray(soba.sporedneSlike)) {
      slike.push(...soba.sporedneSlike.map(s => ({ ...s, glavna: false })));
    }
    return slike;
  }, [soba]);

  const slikeHotela = useMemo(() => {
    const slike = [];
    if (hotel?.glavnaSlika?.putanja) slike.push({ ...hotel.glavnaSlika, glavna: true });
    if (Array.isArray(hotel?.sporedneSlike)) slike.push(...hotel.sporedneSlike.map(s => ({ ...s, glavna: false })));
    return slike;
  }, [hotel]);

  const formatDatum = (datum) => {
    if (!datum) return '';
    const dateDatum = typeof datum === 'string' ? new Date(datum) : datum;
    return dateDatum.toLocaleDateString('hr-HR');
  };

  // hotel - update, delete
  const handleUrediNaziv = (e) =>
    setHotel(prev => ({ ...prev, naziv: e.target.value })); //e - promjena u inputu, target - input, value - tekst inputa

  const handleToggle = (polje) => (e) =>
    setHotel(prev => ({ ...prev, [polje]: e.target.checked }))

  const handleUrediHotel = async (e) => {  //e (type(submit,change), target(input), timeStamp, preventDefault())
    e.preventDefault();
    setPoruka(null);
    try {
      const dto = {
        naziv: hotel.naziv,
        parking: !!hotel.parking,
        wifi: !!hotel.wifi,
        bazen: !!hotel.bazen
      };
      const res = await updateHotel(idHotel, dto);
      if (res?.data) { //res je sam odgovor/objekt, data je tile odgovora, stvarni podaci (JSON)
        setHotel(mapHotel(res.data));
      }
      setPoruka({ type: 'success', text: 'Hotel je uspješno ažuriran.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod ažuriranja hotela.' })
    }
  }

  const handleObrisiHotel = async (e) => {
    e.preventDefault();
    setPoruka(null);
    try {
      await softDeleteHotel(idHotel);
      setPoruka({ type: 'success', text: 'Hotel je postavljen kao neaktivan.' })
      refreshHotel();
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod brisanja hotela.' })
    }
  }

  const handleAktivirajHotel = async (e) => {
    e.preventDefault();
    setPoruka(null);
    try {
      await aktivirajHotel(idHotel);
      refreshHotel();
      setPoruka({ type: 'success', text: 'Hotel je aktiviran.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod aktivacije hotela.' })
    }
  }

  //slike hotela - add, update, delete
  const handleUrediSlikaPutanja = (e) => {
    setSlika(prev => ({ ...prev, putanja: e.target.value })); //target - element koji je kliknut, currentTarget - element na kojem je target vezan
  };

  const handleUrediSlikaGlavna = (e) => {
    setSlika(prev => ({ ...prev, glavna: e.target.checked }));
  };

  const handleAddSlikaH = async (e) => {
    e.preventDefault();
    setPoruka(null);
    try {
      const dto = {
        putanja: slika.putanja.trim(),
        glavna: !!slika.glavna
      };

      if (!dto.putanja) {
        setPoruka({ type: 'danger', text: 'Unesite putanju slike.' });
        return;
      };

      await addSlikaH(idHotel, slika);
      await refreshHotel();
      setSlika({ putanja: '', glavna: false });

      setPoruka({ type: 'success', text: 'Slika hotela uspješno dodana.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod dodavanja slike hotela.' })
    }
  };

  const handleDeleteSlikaH = async (idSlika) => {
    if (!window.confirm('Želite li obrisati ovu sliku hotela?')) {
      return;
    }
    setPoruka(null);
    try {
      await deleteSlikaH(idSlika);
      await refreshHotel();

      setPoruka({ type: 'success', text: 'Slika hotela uspješno obrisana.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod brisanja slike hotela.' })
    }
  }

  const handleSetGlavnaH = async (idSlika) => {
    setPoruka(null);
    try {
      await setGlavnaH(idSlika);
      await refreshHotel();

      setPoruka({ type: 'success', text: 'Uspješno postavljena nova glavna slika.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod postavljanja slike kao hotela kao glavne.' })
    }
  }

  // soba - add, update, softDelete
  const resetSobaDto = () =>
    setSobaDto({
      id: '',
      brojSobe: '',
      kapacitet: '',
      cijenaNocenja: '',
      balkon: false,
      petFriendly: false
    });

  const promjenaKapacitetSoba = (e) => {
    setSobaDto(prev => ({ ...prev, kapacitet: e.target.value }))
  };

  const promjenaCijenaNocenjaSoba = (e) => {
    setSobaDto(prev => ({ ...prev, cijenaNocenja: e.target.value }))
  };

  const promjenaBrojSobe = (e) => {
    setSobaDto(prev => ({ ...prev, brojSobe: e.target.value }))
  };

  const toggleDetalji = (e) => {
    const { name, checked } = e.target;
    setSobaDto(prev => ({ ...prev, [name]: checked }))
  };

  const otvoriModalDodajS = () => {
    resetSobaDto();
    setModalDodajS(true);
  };

  const otvoriModalUrediS = (soba) => {
    setUrediSobaId(soba.id);
    setSobaDto({
      id: soba.id,
      brojSobe: soba.brojSobe,
      kapacitet: soba.kapacitet,
      cijenaNocenja: soba.cijenaNocenja,
      balkon: !!soba.balkon,
      petFriendly: !!soba.petFriendly
    });
    setModalUrediS(true);
  };

  const otvoriModalUrediSlikeS = (idSoba) => {
    setSobaId(idSoba);
    setSlika({
      putanja: '',
      glavna: false
    });
    setModalUrediSlikeS(true);
  };

  const handleDodajSobu = async (e) => {
    e.preventDefault();
    setPoruka(null);
    try {
      const dto = ({
        kapacitet: sobaDto.kapacitet,
        cijenaNocenja: sobaDto.cijenaNocenja,
        brojSobe: sobaDto.brojSobe,
        balkon: !!sobaDto.balkon,
        petFriendly: !!sobaDto.petFriendly
      });

      await addSoba(idHotel, dto);

      setModalDodajS(false);
      setPoruka({ type: 'success', text: 'Nova soba uspješno dodana.' })

      await refreshSobe();

    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod dodavanja nove sobe.' })
    }
  };

  const handleDeleteSoba = async (idSoba) => {
    setPoruka(null);
    try {
      await softDeleteSoba(idSoba);
      setPoruka({ type: 'success', text: 'Soba uspješno deaktivirana.' })

      await refreshSobe();
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod deaktvacije sobe.' })
    }
  };


  const handleAktivirajSoba = async (idSoba) => {
    setPoruka(null);
    try {
      await aktivirajSoba(idSoba);
      setPoruka({ type: 'success', text: 'Soba uspješno aktivirana' });
      refreshSobe();
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod aktivacije sobe' });
    }
  };

  const handleUrediSoba = async (e) => {
    e.preventDefault();
    setPoruka(null);
    try {
      const dto = {
        kapacitet: sobaDto.kapacitet,
        cijenaNocenja: sobaDto.cijenaNocenja,
        balkon: !!sobaDto.balkon,
        petFriendly: !!sobaDto.petFriendly
      };

      await updateSoba(sobaDto.id, dto);
      setPoruka({ type: 'success', text: 'Soba uspješno ažurirana.' })
      setModalUrediS(false);

      await refreshSobe();
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod uređivanja sobe.' })
    }
  };

  const handleAddSlikaS = async (e) => {
    e.preventDefault();
    if (!sobaId) return;
    setPoruka(null);
    try {
      const dto = ({
        putanja: slika.putanja.trim(),
        glavna: !!slika.glavna
      });

      if (!dto.putanja) {
        setPoruka({ type: 'danger', text: 'Unesite putanju slike.' })
        return;
      };

      await addSlikaS(sobaId, dto);
      setPoruka({ type: 'success', text: 'Slika sobe uspješno dodana.' });

      await refreshSoba();
      await refreshSobe();
      setSlika({ putanja: '', glavna: false });

    } catch (err) {
      console.error(err);
      setPoruka({ type: 'danger', text: 'Greška kod dodavanja slike sobe.' })
    }
  };

  const handleSetGlavnaS = async (idSlika) => {
    setPoruka(null);
    try {
      await setGlavnaS(idSlika);
      await refreshSoba();
      await refreshSobe();
      setPoruka({ type: 'success', text: 'Uspješno postavljena nova glavna slika.' });

    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod postavljanja glavne slike sobe.' });
    }
  };

  const handleObrisiSlikaS = async (idSlika) => {
    setPoruka(null);
    try {
      await deleteSlikaS(idSlika);
      await refreshSoba();
      await refreshSobe();
      setPoruka({ type: 'success', text: 'Slika hotela uspješno obrisana.' })
    } catch (e) {
      console.error(e);
      setPoruka({ type: 'danger', text: 'Greška kod brisanja slike sobe.' })
    }
  };

  //rezervacije
  const uLokalniDatum = (yyyyMmDd) => (yyyyMmDd ? new Date(`${yyyyMmDd}`) : null);
  const uYMD = (d) => (d instanceof Date && !isNaN(d) ? d.toLocaleDateString('sv-SE') : '');

  const dodajDane = (date, dani) => {
    if (!date) return null;
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    d.setDate(d.getDate() + dani);
    return d;
  };

  function provjeriDatumDolaska(datumPocetak) {
    if (datumPocetak) {
      const danas = new Date(); danas.setHours(0, 0, 0, 0);
      const dolazak = new Date(datumPocetak);
      if (dolazak < danas) return 'Datum dolaska ne može biti prije današnjeg datuma!';
      return '';
    }
    return 'Unesite datum dolaska!';
  }

  function provjeriDatumOdlaska(datumPocetak, datumKraj) {
    if (datumKraj) {
      const dolazak = new Date(datumPocetak);
      const odlazak = new Date(datumKraj);
      if (isNaN(dolazak.getTime())) return 'Prvo unesite datum dolaska!';
      if (odlazak <= dolazak) return 'Datum odlaska mora biti nakon datuma dolaska!';
      return '';
    }
    return 'Unesite datum odlaska!';
  }

  function provjeriBrojOsoba(broj, kapacitet) {
    const n = Number(broj);
    if (!Number.isInteger(n) || n < 1) return 'Unesite barem 1 osobu.';
    if (kapacitet && n > kapacitet) return `Maksimalno ${kapacitet} osoba/e.`;
    return '';
  }

  function provjeriUnos() {
    const e = {
      datumPocetak: provjeriDatumDolaska(formaRez.datumPocetak),
      datumKraj: provjeriDatumOdlaska(formaRez.datumPocetak, formaRez.datumKraj),
      brojOsoba: provjeriBrojOsoba(formaRez.brojOsoba, odabranaRez?.soba?.kapacitet)
    };
    setUrediRezErr(e);
    return Object.values(e).every((msg) => msg === '');
  }

  const handleRezTablicaClick = (rez) => {
    setOdabranaRez(rez);
    setModalRez(true);
  };

  useEffect(() => {
    if (!(modalRez && odabranaRez)) return;

    const poc = uYMD(new Date(odabranaRez.datumPocetak));
    const kraj = uYMD(new Date(odabranaRez.datumKraj));

    setFormaRez({
      brojOsoba: odabranaRez.brojOsoba,
      datumPocetak: poc,
      datumKraj: kraj
    });

    setPocetniDatum(uLokalniDatum(poc));
    setZavrsniDatum(uLokalniDatum(kraj));
    setUrediRezErr({ datumPocetak: '', datumKraj: '', brojOsoba: '' });

    (async () => {
      try {
        const res = await getZauzetiDatumi(odabranaRez.soba.id);
        const termini = Array.isArray(res.data) ? res.data : [];
        // izuzmi vlastitu rezervaciju iz blokada
        const bezMoje = termini.filter(t => (t.idRezervacija ?? t.id) !== odabranaRez.id);
        setZauzetiTermini(bezMoje);
      } catch (e1) {
        console.error('Greška kod dohvaćanja zauzetih termina!', e1);
        setZauzetiTermini([]);
      }
    })();
  }, [modalRez, odabranaRez]);

  const promjenaForme = (e) =>
    setFormaRez((staro) => ({ ...staro, [e.target.name]: e.target.value }));

  const promjenaPocetka = (date) => {
    setPocetniDatum(date);
    setFormaRez(prev => ({ ...prev, datumPocetak: uYMD(date) }));
    const minKraj = dodajDane(date, 1);
    if (zavrsniDatum && minKraj && zavrsniDatum < minKraj) {
      setZavrsniDatum(null);
      setFormaRez(prev => ({ ...prev, datumKraj: '' }));
    }
    setUrediRezErr(prev => ({ ...prev, datumPocetak: '' }));
  };

  const promjenaKraja = (date) => {
    setZavrsniDatum(date);
    setFormaRez(prev => ({ ...prev, datumKraj: uYMD(date) }));
    setUrediRezErr(prev => ({ ...prev, datumKraj: '' }));
  };

  const zabranjeniIntervali = zauzetiTermini.map(t => {
    const start = uLokalniDatum(t.datumPocetak);
    const endExclusive = uLokalniDatum(t.datumKraj);
    const endInclusive = new Date(endExclusive.getTime() - 86400000); // checkout dan slobodan
    return { start, end: endInclusive };
  });

  const urediRezervaciju = async () => {
    if (!odabranaRez) return;
    if (!provjeriUnos()) return;

    const updateDto = {
      brojOsoba: Number(formaRez.brojOsoba),
      datumPocetak: formaRez.datumPocetak,
      datumKraj: formaRez.datumKraj
    };

    try {
      await adminUpdateRezervacija(odabranaRez.id, updateDto);

      // lokalno ažuriraj listu rezervacija (ovdje sve rezervacije hotela)
      setRezervacije(stare =>
        stare.map(r => (r.id === odabranaRez.id ? { ...r, ...updateDto } : r))
      );
      setOdabranaRez(prev => (prev ? { ...prev, ...updateDto } : prev));

      setPoruka({ type: 'success', text: 'Rezervacija ažurirana.' });
      setModalRez(false);
    } catch (e1) {
      console.error('Greška kod ažuriranja rezervacije!', e1);
      setPoruka({ type: 'danger', text: 'Greška kod ažuriranja rezervacije.' });
    }
  };

  const obrisiRezervaciju = async () => {
    if (!odabranaRez) return;
    try {
      await adminDeleteRezervacija(odabranaRez.id);
      setRezervacije(stare => stare.filter(r => r.id !== odabranaRez.id));
      setPoruka({ type: 'success', text: 'Rezervacija obrisana.' });
      setModalRez(false);
      setOdabranaRez(null);
    } catch (e1) {
      console.error('Greška kod brisanja rezervacije!', e1);
      setPoruka({ type: 'danger', text: 'Greška kod brisanja rezervacije.' });
    }
  };


  useEffect(() => {
    (async () => {
      try {
        setUcitavanje(true);
        const [hotelRes, sobeRes, rezRes] = await Promise.allSettled([ //status, value/reason
          getHotel(idHotel),
          getSobeHotela(idHotel),
          getRezervacijaHotela(idHotel)
        ]);

        if (hotelRes.status === 'fulfilled') {
          setHotel(mapHotel(hotelRes.value.data));
        } else {
          setPoruka({ type: 'danger', text: 'Greška kod učitavanja hotela.' });
        }

        if (sobeRes.status === 'fulfilled') {
          setSobe(sobeRes.value.data ?? []); //osiguram da je sobe uvijek niz, pa npr. sobe.map() ne pukne
        } else {
          setSobe([]);
        }

        if (rezRes.status === 'fulfilled') {
          setRezervacije(rezRes.value.data ?? []);
        } else {
          setRezervacije([]);
        }

      } catch (e) {
        console.error(e);
        setPoruka({ type: 'danger', text: 'Neočekivana greška kod učitavanja.' })
      } finally {
        setUcitavanje(false);
      }
    })();
  }, [idHotel]);

  if (ucitavanje) {
    return <div className='container py-5 mt-5'>Učitavanje...</div>
  }

  return (
    <div className='container py-4'>
      {poruka && <div className={`alert alert-${poruka.type} mb-3`}>{poruka.text}</div>}

      {/* Uredi hotel */}
      <form onSubmit={handleUrediHotel} className='card shadow-sm border-0 mb-4' style={{ borderRadius: 12 }}>
        <div className='card-body'>
          <div className='d-flex align-items-center justify-content-between m-3'>
            <h4 className='m-0'>Uredi hotel</h4>
            <div className='d-flex gap-2'>
              <button type='submit' className='btn btn-primary'>Spremi promjene</button>
              {!!hotel.aktivan ? (
                <button type='button' className='btn btn-outline-danger' onClick={handleObrisiHotel}>Obriši hotel</button>
              ) : (
                <button type='button' className='btn btn-outline-success' onClick={handleAktivirajHotel}>Aktiviraj hotel</button>
              )}
            </div>
          </div>

          <div className='row g-4 align-items-end'>
            <div className='col-md-6'>
              <label id='naziv' className='form-label'>Naziv</label>
              <input
                type="text"
                id='naziv'
                className='form-control'
                value={hotel.naziv}
                placeholder='Unesite naziv hotela'
                onChange={handleUrediNaziv}
              />
            </div>
            <div className='col-md-6'>
              <div className='d-flex gap-4 flex-wrap'>
                <div className='form-check'>
                  <input
                    type="checkbox"
                    id="parking"
                    className='form-check-input'
                    checked={!!hotel.parking}
                    onChange={handleToggle('parking')}
                  />
                  <label className='form-check-label' htmlFor='parking'>Parking</label>
                </div>

                <div className='form-check'>
                  <input
                    type="checkbox"
                    id="bazen"
                    className='form-check-input'
                    checked={!!hotel.bazen}
                    onChange={handleToggle('bazen')}
                  />
                  <label className='form-check-label' htmlFor='parking'>Bazen</label>
                </div>

                <div className='form-check'>
                  <input
                    type="checkbox"
                    id="wifi"
                    className='form-check-input'
                    checked={!!hotel.wifi}
                    onChange={handleToggle('wifi')}
                  />
                  <label className='form-check-label' htmlFor='wifi'>Wifi</label>
                </div>
              </div>
            </div>
          </div>

          <div className='row g-4 mt-3'>
            <div className='col-md-6'>
              <label className='form-label'>Grad</label>
              <input
                className='form-control'
                value={hotel.grad}
                disabled
              />
            </div>
            <div className='col-md-6'>
              <label className='form-label'>Adresa</label>
              <input
                className='form-control'
                value={hotel.adresa}
                disabled
              />
            </div>
          </div>
        </div>
      </form>

      {/* uredi slike hotela */}
      <div className='card shadow-sm border-0 mb-4' style={{ borderRadius: 12 }}>
        <div className='card-body'>
          <h4 className='mb-3'>Uredi slike hotela</h4>

          <form className='row g-3 align-items-end mb-4' onSubmit={handleAddSlikaH}>
            <div className='col-md-8'>
              <label className='form-label'>Putanja</label>
              <input
                type="url"
                name="putanja"
                className='form-control'
                placeholder='http://...'
                value={slika.putanja}
                onChange={handleUrediSlikaPutanja}
                required
              />
            </div>
            <div className='col-md-2 d-flex align-items-center'>
              <div className='form-check mt-4'>
                <input
                  type="checkbox"
                  id='slika-glavna'
                  name='glavna'
                  className='form-check-input'
                  checked={!!slika.glavna}
                  onChange={handleUrediSlikaGlavna}
                />
                <label htmlFor="slika-glavna" className='form-check-label'>Glavna slika</label>
              </div>
            </div>
            <div className='col-md-2'>
              <button type='submit' className='btn btn-primary w-100 mt-4'>Dodaj sliku</button>
            </div>
          </form>

          {slikeHotela.length === 0 ? (
            <div className='text-muted'>Hotel nema slika.</div>
          ) : (
            <div className='row g-3'>
              {slikeHotela.map((slika, index) => (
                <div className='col-md-3' key={slika.id}>
                  <div className='card border-0 shadow-sm h-100' style={{ borderRadius: 12, overflow: 'hidden' }}>
                    <img src={slika.putanja} alt={`Slika ${index + 1}`} style={{ height: 160, width: '100%', objectFit: 'cover' }} />
                    <div className='card-body d-flex flex-column'>
                      <span className={`badge ${slika.glavna ? 'bg-success' : 'bg-secondary'} align-self-start mb-2`}>
                        {slika.glavna ? 'Glavna' : 'Sporedna'}
                      </span>

                      <div className='mt-auto d-flex gap-2'>
                        {!slika.glavna && (
                          <button type='button' className='btn btn-outline-primary btn-sm' onClick={() => handleSetGlavnaH(slika.id)}>
                            Postavi kao glavnu
                          </button>
                        )}
                        <button type='button' className='btn btn-outline-danger btn-sm' onClick={() => handleDeleteSlikaH(slika.id)}>
                          Obriši
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

      {/* sobe hotela */}
      <div className='card shadow-sm border-0 mb-4' style={{ borderRadius: 12 }}>
        <div className='card-body'>
          <div className='d-flex align-items-center justify-content-between mb-3'>
            <h4 className='m-0'>Sobe</h4>
            <button className='btn btn-primary' onClick={otvoriModalDodajS}>Dodaj novu sobu</button>
          </div>

          {sobe.length === 0 ? (
            <div className='text-muted'>Hotel nema dodanih soba.</div>
          ) : (
            <div className='row g-4'>
              {sobe.map((soba) => (
                <div key={soba.id} className='col-md-4'>
                  <div className='card h-100 border-0 shadow-sm' style={{ borderRadius: 12 }}>
                    {soba.glavnaSlika?.putanja && (
                      <img src={soba.glavnaSlika.putanja} alt="Glavna slika sobe" className='card-img-top' style={{ height: 200, objectFit: 'cover' }} />
                    )}
                    <div className='card-body d-flex flex-column'>
                      <div className='d-flex justify-content-between align-items-center mb-2'>
                        <h5 className='card-title m-0'>Soba {soba.brojSobe}</h5>
                        <span className={`badge ${soba.aktivno ? 'bg-success' : 'bg-secondary'}`}>
                          {soba.aktivno ? 'Aktivna' : 'Neaktivna'}
                        </span>
                      </div>
                      <ul className='list-unstyled mb-3'>
                        <li><strong>Kapacitet:</strong> {soba.kapacitet} osoba/e</li>
                        <li><strong>Cijena/noć:</strong> {soba.cijenaNocenja} €</li>
                        <li><strong>Balkon:</strong> {soba.balkon ? 'Da' : 'Ne'}</li>
                        <li><strong>Pet Friendly:</strong> {soba.petFriendly ? 'Da' : 'Ne'}</li>
                      </ul>

                      <div className='mt-auto d-flex gap-2'>
                        <button className='btn btn-outline-secondary btn-sm' onClick={() => otvoriModalUrediSlikeS(soba.id)}>Uredi slike</button>
                        <button className='btn btn-outline-primary btn-sm' onClick={() => otvoriModalUrediS(soba)}>Uredi sobu</button>
                        {soba.aktivno ? (
                          <button className='btn btn-outline-danger btn-sm' onClick={() => handleDeleteSoba(soba.id)}>Deaktiviraj sobu</button>
                        ) : (
                          <button className='btn btn-outline-success btn-sm' onClick={() => handleAktivirajSoba(soba.id)}>Aktiviraj sobu</button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      <div className='card shadow-sm border-0 mb-4' style={{ borderRadius: 12 }}>
        <div className='card-body'>
          <div className='d-flex align-items-center justify-content-between mb-3'>
            <h4 className='m-0'>Rezervacije</h4>
          </div>

          {rezervacije.length === 0 ? (
            <div className='text-muted'>Hotel nema rezervacija.</div>
          ) : (
            <div className='table-responsive'>
              <table className='table table-hover align-middle'>
                <thead>
                  <tr>
                    <th>Soba</th>
                    <th>Osoba/e</th>
                    <th>Dolazak</th>
                    <th>Odlazak</th>
                  </tr>
                </thead>
                <tbody>
                  {rezervacije.map((rez) => (
                    <tr
                      key={rez.id}
                      style={{ cursor: 'pointer' }}
                      role='button'
                      onClick={() => handleRezTablicaClick(rez)}
                    >
                      <td>{rez.soba.brojSobe}</td>
                      <td>{rez.brojOsoba}</td>
                      <td>{formatDatum(rez.datumPocetak)}</td>
                      <td>{formatDatum(rez.datumKraj)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* modali */}

      {/* dodaj sobu */}

      {modalDodajS && (
        <>
          <div className='modal-backdrop fade show' onClick={() => setModalDodajS(false)} ></div>
          <div className='modal show d-block' role='dialog' aria-modal='true'>
            <div className='modal-dialog' onClick={(e) => e.stopPropagation()}>
              <div className='modal-content'>
                <form onSubmit={handleDodajSobu}>
                  <div className='modal-header'>
                    <h5 className='modal-title'>Dodaj sobu</h5>
                    <button type='button' className='btn-close' onClick={() => setModalDodajS(false)}></button>
                  </div>
                  <div className='modal-body'>
                    <div className='row g-3'>
                      <div className='col-6'>
                        <label className='form-label'>Broj sobe</label>
                        <input
                          type="number"
                          name="brojSobe"
                          className='form-control'
                          value={sobaDto.brojSobe}
                          onChange={promjenaBrojSobe}
                          required
                        />
                      </div>
                      <div className='col-6'>
                        <label className='form-label'>Kapacitet</label>
                        <input
                          type="number"
                          className='form-control'
                          name='kapacitet'
                          min={1}
                          value={sobaDto.kapacitet}
                          onChange={promjenaKapacitetSoba}
                          required
                        />
                      </div>
                      <div className='col-6'>
                        <label className='form-label'>Cijena/noć (€)</label>
                        <input
                          type="number"
                          step={0.01}
                          className='form-control'
                          min={0}
                          value={sobaDto.cijenaNocenja}
                          onChange={promjenaCijenaNocenjaSoba}
                          required
                        />
                      </div>
                      <div className='col-6 d-flex align-items-center gap-4'>
                        <div className='form-check'>
                          <input id='balkon' type="checkbox" name='balkon' className='form-check-input'
                            checked={!!sobaDto.balkon} onChange={toggleDetalji}
                          />
                          <label htmlFor="balkon" className='form-check-label'>Balkon</label>
                        </div>
                        <div className='form-check'>
                          <input id='petFriendly' type="checkbox" name='petFriendly' className='form-check-input'
                            checked={!!sobaDto.petFriendly} onChange={toggleDetalji}
                          />
                          <label htmlFor="petFriendly" className='form-check-label'>Pet Friendly</label>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className='modal-footer'>
                    <button type='button' className='btn btn-outline-secondary' onClick={() => setModalDodajS(false)}>Odustani</button>
                    <button type='submit' className='btn btn-primary'>Dodaj</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </>
      )}

      {/* uredi sobu */}

      {modalUrediS && (
        <>
          <div className='modal-backdrop fade show' onClick={() => setModalUrediS(false)}></div>
          <div className='modal fade show d-block' role='dialog' aria-modal='true'>
            <div className='modal-dialog' onClick={(e) => e.stopPropagation()}>
              <div className='modal-content'>
                <form onSubmit={handleUrediSoba}>
                  <div className='modal-header'>
                    <h5 className='modal-title'>Uredi sobu #{sobaDto.brojSobe}</h5>
                    <button className='btn-close' type='button' onClick={() => setModalUrediS(false)}></button>
                  </div>
                  <div className='modal-body'>
                    <div className='row g-3'>
                      <div className='col-6'>
                        <label className='form-label'>Broj sobe</label>
                        <input
                          type="number"
                          name="brojSobe"
                          className='form-control'
                          value={sobaDto.brojSobe}
                          disabled
                        />
                      </div>
                      <div className='col-6'>
                        <label className='form-label'>Kapacitet</label>
                        <input
                          type="number"
                          name='kapacitet'
                          min={1}
                          className='form-control'
                          value={sobaDto.kapacitet}
                          onChange={promjenaKapacitetSoba}
                        />
                      </div>
                      <div className='col-6'>
                        <label className='form-label'>Cijena/noć (€)</label>
                        <input
                          type="number"
                          step={0.01}
                          name='cijenaNocenja'
                          min={0}
                          className='form-control'
                          value={sobaDto.cijenaNocenja}
                          onChange={promjenaCijenaNocenjaSoba}
                          required
                        />
                      </div>
                      <div className='col-6 d-flex align-items-center gap-4'>
                        <div className='form-check'>
                          <input type="checkbox" id='balkonCheck' name='balkon' className='form-check-input'
                            checked={!!sobaDto.balkon} onChange={toggleDetalji} />
                          <label htmlFor="balkonCheck" className='form-check-label'>Balkon</label>
                        </div>
                        <div className='form-check'>
                          <input type="checkbox" id='petFriendlyCheck' name='petFriendly' className='form-check-input'
                            checked={!!sobaDto.petFriendly} onChange={toggleDetalji} />
                          <label htmlFor="petFriendlyCheck" className='form-check-label'>Pet Friendly</label>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className='modal-footer'>
                    <button type='submit' className='btn btn-outline-secondary' onClick={() => setModalUrediS(false)}>Odustani</button>
                    <button type='submit' className='btn btn-primary'>Potvrdi</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </>
      )}

      {/* uredi slike sobe */}

      {modalUrediSlikeS && (
        <>
          <div className="modal-backdrop fade show" onClick={() => setModalUrediSlikeS(false)} />
          <div className="modal fade show d-block" role="dialog" aria-modal="true">
            <div className="modal-dialog modal-lg" onClick={(e) => e.stopPropagation()}>
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">
                    {soba ? `Uredi slike - soba ${soba.brojSobe}` : 'Učitavanje…'}
                  </h5>
                  <button className="btn-close" onClick={() => setModalUrediSlikeS(false)} />
                </div>

                <div className="modal-body">
                  {!soba ? (
                    <div className="text-muted">Učitavanje...</div>
                  ) : (
                    <>
                      <form onSubmit={handleAddSlikaS} className='row g-3 align-items-end mb-3'>
                        <div className='col-md-8'>
                          <label className='form-label'>Putanja slike</label>
                          <input
                            type="url"
                            name="putanja"
                            className='form-control'
                            placeholder='http://...'
                            value={slika.putanja}
                            onChange={handleUrediSlikaPutanja}
                            required
                          />
                        </div>
                        <div className='col-md-2 d-flex align-items-center'>
                          <div className='form-check mt-4'>
                            <input
                              type="checkbox"
                              id='glavnaSlika'
                              name='glavna'
                              className='form-check-input'
                              checked={!!slika.glavna}
                              onChange={handleUrediSlikaGlavna}
                            />
                            <label htmlFor="glavnaSlika" className='form-check-label'>Glavna slika</label>
                          </div>
                        </div>
                        <div className='col-md-2'>
                          <button type='submit' className='btn btn-primary w-100 mt-4 btn-sm'>Dodaj</button>
                        </div>
                      </form>

                      {slikeSobe.length === 0 ? (
                        <div className='text-muted'>Soba nema slika.</div>
                      ) : (
                        <div className='row g-3'>
                          {slikeSobe.map((slika, index) => (
                            <div className='col-md-4' key={slika.id}>
                              <div className='card h-100' style={{ borderRadius: 12, overflow: 'hidden' }}>
                                <img src={slika.putanja} alt={`Slika ${index + 1}`} style={{ height: 160, width: '100%', objectFit: 'cover' }} />
                                <div className='card-body d-flex flex-column'>
                                  <span className={`badge ${slika.glavna ? 'bg-success' : 'bg-secondary'} align-self-start mb-2`}>
                                    {slika.glavna ? 'Glavna' : 'Sporedna'}
                                  </span>
                                  <div className='mt-auto d-flex gap-2'>
                                    {!slika.glavna && (
                                      <button type='button' className='btn btn-outline-primary btn-sm' onClick={() => handleSetGlavnaS(slika.id)}>Nova glavna</button>
                                    )}
                                    <button type='buton' className='btn btn-outline-danger btn-sm' onClick={() => handleObrisiSlikaS(slika.id)}>Obriši</button>
                                  </div>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      )}
                    </>
                  )}
                </div>
              </div>
            </div>
          </div>
        </>
      )}

      {modalRez && odabranaRez && (
        <div
          className="modal fade show d-block"
          tabIndex={-1}
          role="dialog"
          style={{ backgroundColor: 'rgba(0,0,0,.5)' }}
          onClick={() => { setModalRez(false); setOdabranaRez(null); }}
        >
          <div
            className="modal-dialog modal-lg modal-dialog-centered"
            role="document"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  Rezervacija – soba {odabranaRez.soba?.brojSobe}
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  aria-label="Zatvori"
                  onClick={() => { setModalRez(false); setOdabranaRez(null); }}
                />
              </div>

              <div className="modal-body">
                <form className="row g-3">
                  <div className="col-sm-4">
                    <label className="form-label">Osoba/e</label>
                    <input
                      type="number"
                      min={1}
                      max={odabranaRez.soba?.kapacitet}
                      name="brojOsoba"
                      className={`form-control ${urediRezErr.brojOsoba ? 'is-invalid' : ''}`}
                      value={formaRez.brojOsoba}
                      onChange={promjenaForme}
                    />
                    {urediRezErr.brojOsoba ? (
                      <div className="invalid-feedback d-block">{urediRezErr.brojOsoba}</div>
                    ) : (
                      <small className='text-muted'>Maksimalno {odabranaRez.soba?.kapacitet} osoba/e</small>
                    )}
                  </div>

                  <div className="col-sm-4">
                    <label className="form-label">Dolazak</label>
                    <br />
                    <DatePicker
                      selected={pocetniDatum}
                      onChange={promjenaPocetka}
                      selectsStart
                      startDate={pocetniDatum}
                      endDate={zavrsniDatum}
                      minDate={new Date()}
                      excludeDateIntervals={zabranjeniIntervali}
                      dateFormat="yyyy-MM-dd"
                      placeholderText="Odaberite datum dolaska"
                      className={`form-control ${urediRezErr.datumPocetak ? 'is-invalid' : ''}`}
                    />
                    {urediRezErr.datumPocetak && (
                      <div className="invalid-feedback d-block">{urediRezErr.datumPocetak}</div>
                    )}
                  </div>

                  <div className="col-sm-4">
                    <label className="form-label">Odlazak</label>
                    <br />
                    <DatePicker
                      selected={zavrsniDatum}
                      onChange={promjenaKraja}
                      selectsEnd
                      startDate={pocetniDatum}
                      endDate={zavrsniDatum}
                      minDate={pocetniDatum ? dodajDane(pocetniDatum, 1) : new Date()}
                      excludeDateIntervals={zabranjeniIntervali}
                      dateFormat="yyyy-MM-dd"
                      placeholderText="Odaberite datum odlaska"
                      className={`form-control ${urediRezErr.datumKraj ? 'is-invalid' : ''}`}
                    />
                    {urediRezErr.datumKraj && (
                      <div className="invalid-feedback d-block">{urediRezErr.datumKraj}</div>
                    )}
                  </div>
                </form>
              </div>

              <div className="modal-footer">
                <button className="btn btn-outline-secondary" onClick={() => { setModalRez(false); setOdabranaRez(null); }}>
                  Odustani
                </button>
                <button className="btn btn-outline-primary" onClick={urediRezervaciju}>
                  Uredi
                </button>
                <button className="btn btn-outline-danger" onClick={obrisiRezervaciju}>
                  Obriši
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

    </div>
  )
}

export default UrediHotel