CREATE TABLE estado (
  id bigint(20) PRIMARY KEY NOT NULL ,
  nome varchar(35) NOT NULL,
  uf char(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE cidade (
  id bigint(20) PRIMARY KEY NOT NULL,
  id_estado bigint(20) NOT NULL,
  nome varchar(32) NOT NULL,
  uf varchar(2) NOT NULL,
  codigo_ibge bigint(20) NOT NULL,
  capital boolean NOT NULL,
  FOREIGN KEY (id_estado) REFERENCES estado (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8