INSERT INTO postinumeroalue (postinro, postitmp) VALUES ('00550','Helsinki');
INSERT INTO postinumeroalue (postinro, postitmp) VALUES ('37950','Akaa');
INSERT INTO postinumeroalue (postinro, postitmp) VALUES ('02300','Espoo');
INSERT INTO postinumeroalue (postinro, postitmp) VALUES ('01200','Vantaa');

INSERT INTO asiakas (etunimi, sukunimi, osoite, postinro)
VALUES ('Sirpa','Wilen','Sirpankuja 7 B','02300');
INSERT INTO asiakas (etunimi, sukunimi, osoite, postinro)
VALUES ('Ilkka','Korhonen','Ullantie 67','37950');
INSERT INTO asiakas (etunimi, sukunimi, osoite, postinro)
VALUES ('Ulla','Virtanen','Taatelikuna 7 H','01200');
INSERT INTO asiakas (etunimi, sukunimi, osoite, postinro)
VALUES ('Esko','Kivikoski','Orioninkatu 78 B 23','00550');

INSERT INTO kirja (isbn, nimi, kirjoittaja, painos, kustantaja)
VALUES('12345678-111', 'Lainassa (Ei pit‰isi n‰ky‰)','Keijo Kukkanen','5','WSOY');

INSERT INTO kirja (isbn, nimi, kirjoittaja, painos, kustantaja)
VALUES('12345678-222', 'Palautettu (Pit‰‰ n‰ky‰)','Kalle Kukkonen','6','WSOY');

INSERT INTO kirja (isbn, nimi, kirjoittaja, painos, kustantaja)
VALUES('12345678-333', 'Ei koskaan lainattu (Pit‰‰ n‰ky‰)','Kalle Kukkonen','6','WSOY');

INSERT INTO nide (nidenro, isbn)
VALUES(1, '12345678-111');

INSERT INTO nide (nidenro, isbn)
VALUES(2, '12345678-222');

INSERT INTO nide (nidenro, isbn)
VALUES(1, '12345678-333');

INSERT INTO lainaus (lainauspvm,asiakasnro)
VALUES('2016-12-01',1);

INSERT INTO lainaus (lainauspvm,asiakasnro)
VALUES('2016-11-01',2);

INSERT INTO nidelainaus (lainausnro,isbn, nidenro)
VALUES(1,'12345678-111', 1);

INSERT INTO nidelainaus (lainausnro, isbn, nidenro, palautuspvm)
VALUES (2, '12345678-222', 2, '2016-11-30');
