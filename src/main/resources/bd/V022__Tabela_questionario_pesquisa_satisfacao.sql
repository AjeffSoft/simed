CREATE TABLE questionario (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  tipo_resposta VARCHAR(10),
  id_empresa BIGINT(20),
  id_pergunta BIGINT(20),
  FOREIGN KEY (id_pergunta) REFERENCES pergunta(id),
  FOREIGN KEY (id_empresa) REFERENCES empresa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;