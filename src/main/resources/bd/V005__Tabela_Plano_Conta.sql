CREATE TABLE plano_conta (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(60) NOT NULL,
  tipo varchar(20) NOT NULL,
  situacao boolean
) ENGINE=InnoDB DEFAULT CHARSET=utf8