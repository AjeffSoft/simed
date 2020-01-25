CREATE TABLE conta_empresa (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(20) DEFAULT NULL,
  conta varchar(15) NOT NULL,
  tipo varchar(20) DEFAULT NULL,
  saldo decimal(10,2) DEFAULT NULL,
  valor_pendente decimal(10,2) DEFAULT NULL,
  situacao boolean,
  id_agencia bigint(20) NOT NULL,
  id_empresa bigint(20) NOT NULL,
  FOREIGN KEY (id_empresa) REFERENCES empresa (id),
  FOREIGN KEY (id_agencia) REFERENCES agencia (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8