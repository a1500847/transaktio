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
VALUES('154643-3543','Kukkuluuruu','Keijo Kukkanen','5. painos','WSOY');

INSERT INTO nide (isbn)
VALUES('154643-3543');

INSERT INTO lainaus (lainauspvm,asiakasnro)
VALUES('2016-06-15',3);

INSERT INTO lainaus (lainauspvm,asiakasnro)
VALUES('2016-06-14',1);

INSERT INTO nidelainaus (lainausnro,nidenro)
VALUES(2,3);

   