CREATE TABLE empresa (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(60) NOT NULL,
  fantasia varchar(60) NOT NULL,
  sigla varchar(3) NOT NULL,
  cnpj varchar(20) NOT NULL,
  tipo varchar(20),
  telefone varchar(20),
  email varchar(100),
  logradouro varchar(60),
  bairro varchar(30),
  cep varchar(15),
  complemento varchar(20),
  situacao boolean,
  id_cidade bigint(20) NOT NULL,
  codigo_pagamento bigint(20) ,
  FOREIGN KEY (id_cidade) REFERENCES cidade (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8