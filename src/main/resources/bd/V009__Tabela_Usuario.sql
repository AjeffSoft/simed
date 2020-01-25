CREATE TABLE usuario (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(120) NOT NULL,
    ativo BOOLEAN DEFAULT true,
    id_empresa BIGINT(20),
    id_empresa_atendente BIGINT(20),
    FOREIGN KEY (id_empresa) REFERENCES empresa(id),
    FOREIGN KEY (id_empresa_atendente) REFERENCES empresa(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- CRIAR USUARIO INICIAL: ADMINISTRADOR
--
INSERT INTO usuario (nome, email, senha, ativo) VALUES ('Administrador', 'administrador@ajeff.com.br', '$2a$10$cc5U3jVwwONN7QemhAKof.QqX375GdZrfeGCtpJ1ftOfj1n.H.jTm', 1);


CREATE TABLE usuario_empresa (
  id_usuario bigint(20) NOT NULL,
  id_empresa bigint(20) NOT NULL,
  PRIMARY KEY (id_usuario,id_empresa),
  FOREIGN KEY (id_usuario) REFERENCES usuario (id),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8