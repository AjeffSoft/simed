CREATE TABLE questionario (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  tipo_resposta VARCHAR(10),
  id_empresa BIGINT(20),
  id_pergunta BIGINT(20),
  ativo boolean,
  FOREIGN KEY (id_pergunta) REFERENCES pergunta(id),
  FOREIGN KEY (id_empresa) REFERENCES empresa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE questionario_resposta (
    id_questionario BIGINT(20) NOT NULL,
    id_resposta BIGINT(20) NOT NULL,
    PRIMARY KEY (id_questionario, id_resposta),
    FOREIGN KEY (id_questionario) REFERENCES questionario(id),
    FOREIGN KEY (id_resposta) REFERENCES resposta(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;