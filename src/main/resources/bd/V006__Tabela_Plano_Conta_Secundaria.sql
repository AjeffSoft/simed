CREATE TABLE plano_conta_secundaria (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(60) NOT NULL,
  tipo varchar(20) NOT NULL,
  situacao boolean,
  id_plano_conta bigint(20) NOT NULL,
  FOREIGN KEY (id_plano_conta) REFERENCES plano_conta (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8