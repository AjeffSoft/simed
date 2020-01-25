CREATE TABLE grupo (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- CRIAR GRUPO INICIAL: ADMINISTRADOR
--
INSERT INTO grupo (nome) VALUES ('Administrador');


CREATE TABLE permissao (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50),
    nome VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE usuario_grupo(
    id_usuario BIGINT(20) NOT NULL,
    id_grupo BIGINT(20) NOT NULL,
    PRIMARY KEY (id_usuario, id_grupo),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- SETAR O USUARIO INICIAL COM O GRUPO ADMINISTRADOR
--
INSERT INTO usuario_grupo (id_usuario, id_grupo) values (1,1);

CREATE TABLE grupo_permissao (
    id_grupo BIGINT(20) NOT NULL,
    id_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY (id_grupo, id_permissao),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id),
    FOREIGN KEY (id_permissao) REFERENCES permissao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
