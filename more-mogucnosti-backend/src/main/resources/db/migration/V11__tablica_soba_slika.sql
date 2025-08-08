CREATE TABLE soba_slika (
    id_soba_slika BIGINT PRIMARY KEY AUTO_INCREMENT,
    soba_id BIGINT NOT NULL,
    putanja VARCHAR(255) NOT NULL,
    glavna_slika BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (soba_id) REFERENCES soba(id_soba)
);

INSERT INTO soba_slika (soba_id, putanja, glavna_slika) VALUES
(1, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291525/73865433007-tempoby-hilton-nashville-standard-king_ift2o1.jpg", true),
(1, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291551/53435fdb-1308-4db5-b734-b813bde3ea31_ondjhg.jpg", false),
(1, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291650/372722789_w1g4ks.jpg", false),

(2, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291521/the-guitar-hotel-grand-suite-1-bed-3150x2150_ncvysy.jpg", true),
(2, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291551/hotel_bathroom_tkieho.jpg", false),
(2, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291649/balcony-sea-view-2_resb9d.jpg", false),

(3, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291521/86e685af18659ee9ecca35c465603812_auwj77.jpg", true),
(3, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291551/natural-bathroom-elements-_mktnom.jpg", false),
(3, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291712/SecheSpa_BeautyShot_MineralBath_1_Final-WEB-1920_uzulhc.jpg", false),

(4, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291521/hv_aptsuite-e1649880541353_qfqhst.jpg", true),
(4, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291552/Ritz-Carlton-Sarasota-Bathroom-PUBLICITY-MAIN-2023_so1lik.jpg", false),
(4, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291650/325751499_x4fc0n.jpg", false),

(5, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1753973327/hotel1928_205premiumking_1_alt_revise_rbzsch.jpg", true),
(5, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291551/hotel_20bathroom_20mayfair_20king_20suite_swbpza.jpg", false),
(5, "https://res.cloudinary.com/dcolr4yi2/image/upload/v1754291712/LDV_VDay_2025_Web-1_qhttjj.jpg", false)
