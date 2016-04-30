CREATE TABLE IF NOT EXISTS type_compte(
	id INT PRIMARY KEY,
	nom VARCHAR(25)
);

CREATE TABLE IF NOT EXISTS status_relation(
	id INT PRIMARY KEY,
	nom VARCHAR(200)
);

INSERT OR REPLACE INTO type_compte VALUES (1, 'Publique'), (2, 'Privé'), (3, 'Amis seulement');
INSERT OR REPLACE INTO status_relation VALUES (1, 'Ami'), (2, "Attente de demande d'ami"), (3, 'Refusé'), (4, 'Bloqué');

CREATE TABLE IF NOT EXISTS grille(
	id INT PRIMARY KEY,
	url VARCHAR(100),
	fichier_local VARCHAR(256) /*Si le fichier a été downloader sur android ou le path sur le serveur.*/
);

CREATE TABLE IF NOT EXISTS utilisateur(
	id INT PRIMARY KEY,
	login VARCHAR(25),
	password VARCHAR(32), /*Hash MD5 sans salt. M'en crisse que ca soit pas sécuritaire, c'est mieux que rien et c'est pour un tp anyway*/
	nom VARCHAR(100),
	prenom VARCHAR(100),
	email VARCHAR(100),
	type_compte INT,
	FOREIGN KEY (type_compte) REFERENCES type_compte(id)
);

CREATE TABLE IF NOT EXISTS relation(
	user_1 INT,
	user_2 INT,
	status_relation INT,
	FOREIGN KEY (status_relation) REFERENCES status_relation(id),
	CONSTRAINT PK_relation PRIMARY KEY (user_1, user_2)
);

CREATE TABLE IF NOT EXISTS defi(
	id INT PRIMARY KEY,
	nom VARCHAR(50),
	nb_tours_max INT, 	/*0 = on s'en sacre*/
	score FLOAT,		/*Score donné par les utilisateur*/
	difficulte FLOAT,	/*Score donné par les utilisateur*/
	grille INT,
	FOREIGN KEY (grille) REFERENCES grille(id)
);

CREATE TABLE IF NOT EXISTS partie(
	id INT PRIMARY KEY,
	elo_blanc INT, /*Au debut de la partie*/
	elo_noir INT,
	gain_blanc INT,
	gain_noir INT,
	grille INT,
	gagnant INT,
	noir INT,
	blanc INT,
	FOREIGN KEY (grille) REFERENCES grille(id),
	FOREIGN KEY (gagnant) REFERENCES utilisateur(id),
	FOREIGN KEY (noir) REFERENCES utilisateur(id),
	FOREIGN KEY (blanc) REFERENCES utilisateur(id)
);

CREATE TABLE IF NOT EXISTS tours(
	partie INT,
	tour INT,
	description VARCHAR(10),
	CONSTRAINT PK_tour PRIMARY KEY (partie, tour),
	FOREIGN KEY (partie) REFERENCES partie(id)
);