CREATE TABLE banco (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(60) NOT NULL,
  codigo varchar(5) NOT NULL,
  situacao boolean
) ENGINE=InnoDB DEFAULT CHARSET=utf8