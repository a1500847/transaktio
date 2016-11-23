CREATE TABLE postinumeroalue
(
	postinro  	CHAR (5)  	NOT NULL PRIMARY KEY,
	postitmp  	VARCHAR(30)  	NOT NULL
)ENGINE=InnoDB;

CREATE TABLE asiakas
(
	numero 		INT NOT NULL AUTO_INCREMENT,
	etunimi   	VARCHAR (30) NOT NULL,
	sukunimi        VARCHAR (30) NOT NULL,
	osoite	 	VARCHAR (30) NOT NULL,
	postinro  	CHAR (5)     NOT NULL,
	PRIMARY KEY (numero),
	FOREIGN KEY (postinro) REFERENCES postinumeroalue (postinro)
)ENGINE=InnoDB;

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

CREATE TABLE tili 
(
	tilinro VARCHAR(15) NOT NULL PRIMARY KEY,
	saldo DECIMAL (8,2) NOT NULL,
	omistaja INT NOT NULL,
	FOREIGN KEY (omistaja) REFERENCES asiakas(numero)
)ENGINE=InnoDB;

INSERT INTO tili (tilinro, saldo, omistaja)
VALUES ('FI99 1234 1234',1000.00, 10000);
INSERT INTO tili (tilinro, saldo, omistaja)
VALUES ('FI99 4321 4321',2500.00, 10001);
INSERT INTO tili (tilinro, saldo, omistaja)
VALUES ('FI99 5432 5432',1500.50, 10002);
INSERT INTO tili (tilinro, saldo, omistaja)
VALUES ('FI99 9876 9876',2003.30, 10005);

SELECT tilinro, saldo,a.numero AS asiakasnumero, 
   etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp, 
   FROM tili  JOIN asiakas a ON omistaja=a.numero 
   JOIN postinumeroalue p ON p.postinro=a.postinro 
   ORDER BY tilinro;
   
SELECT tilinro, saldo, a.numero AS asiakasnro, 
    etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp
from tili join asiakas a ON omistaja = a.numero 
JOIN postinumeroalue p ON p.postinro=a.postinro
ORDER BY tilinro;
   