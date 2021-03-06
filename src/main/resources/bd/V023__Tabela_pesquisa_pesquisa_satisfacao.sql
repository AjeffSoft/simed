CREATE TABLE pesquisa (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  data DATETIME,
  anotacoes VARCHAR(250),
  id_usuario BIGINT(20),
  fechado boolean,
  FOREIGN KEY (id_usuario) REFERENCES usuario(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE pesquisa_item (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  nota VARCHAR(15),
  id_pesquisa BIGINT(20),
  id_questionario BIGINT(20),
  id_resposta BIGINT(20),
  FOREIGN KEY (id_resposta) REFERENCES resposta(id),
  FOREIGN KEY (id_pesquisa) REFERENCES pesquisa(id),
  FOREIGN KEY (id_questionario) REFERENCES questionario(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;




