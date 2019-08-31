drop table pesquisa;
drop table questionario;
drop table pergunta;

CREATE TABLE pesquisa (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  data DATETIME,
  cliente VARCHAR(100),
  id_usuario BIGINT(20),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE questao (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(200),
  tipo_resposta VARCHAR(10),
  id_empresa BIGINT(20),
  FOREIGN KEY (id_empresa) REFERENCES empresa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE resposta (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(200),
  tipo_resposta VARCHAR(10)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE pergunta (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  id_resposta BIGINT(20),
  id_questao BIGINT(20),
  id_pesquisa BIGINT(20),
  FOREIGN KEY (id_resposta) REFERENCES resposta(id),
  FOREIGN KEY (id_questao) REFERENCES questao(id),
  FOREIGN KEY (id_pesquisa) REFERENCES pesquisa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
