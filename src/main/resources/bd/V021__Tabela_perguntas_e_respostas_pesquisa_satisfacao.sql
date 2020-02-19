drop table questionario;
drop table pesquisa;
drop table pergunta;

CREATE TABLE pergunta (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(200)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE resposta (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(200)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
