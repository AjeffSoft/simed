CREATE TABLE agencia (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  agencia varchar(10) NOT NULL,
  dv varchar(2),
  situacao boolean,
  id_banco bigint(20) NOT NULL,
  FOREIGN KEY (id_banco) REFERENCES banco (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8