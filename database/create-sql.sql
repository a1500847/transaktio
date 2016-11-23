CREATE TABLE postinumeroalue
(
	postinro  	CHAR (5)  	NOT NULL PRIMARY KEY,
	postitmp  	VARCHAR(30)  	NOT NULL
)ENGINE=InnoDB;

CREATE TABLE asiakas(
	numero INT NOT NULL AUTO_INCREMENT
	,etunimi   	VARCHAR (30) NOT NULL
	,sukunimi        VARCHAR (30) NOT NULL
	,osoite	 	VARCHAR (30) NOT NULL
	,postinro  	CHAR (5)     NOT NULL
	,PRIMARY KEY (numero)
	,FOREIGN KEY (postinro) REFERENCES postinumeroalue (postinro)
)ENGINE=InnoDB;

CREATE TABLE kirja (
isbn VARCHAR(20) NOT NULL
,nimi VARCHAR(30) NOT NULL
,kirjoittaja VARCHAR(30) NOT NULL
,painos VARCHAR(20)
,kustantaja VARCHAR(30)
, PRIMARY KEY (isbn)
)ENGINE=InnoDB;

CREATE TABLE nide (
nidenro INT NOT NULL AUTO_INCREMENT 
,isbn VARCHAR(20) NOT NULL
,PRIMARY KEY (nidenro)
,FOREIGN KEY (isbn) REFERENCES kirja (isbn)
)ENGINE=InnoDB;

CREATE TABLE lainaus (
numero INT NOT NULL AUTO_INCREMENT
,lainauspvm DATE NOT NULL
,asiakasnro INT NOT NULL
,PRIMARY KEY (numero)
,FOREIGN KEY (asiakasnro) REFERENCES asiakas (numero) 
)ENGINE=InnoDB;

CREATE TABLE nidelainaus (
lainausnro INT NOT NULL
,nidenro INT NOT NULL
,palautuspvm DATE
,PRIMARY KEY (lainausnro,nidenro)
,FOREIGN KEY (lainausnro) REFERENCES lainaus (numero)
,FOREIGN KEY (nidenro) REFERENCES nide (nidenro)
)ENGINE=InnoDB;