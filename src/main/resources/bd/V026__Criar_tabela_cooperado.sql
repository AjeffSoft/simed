create table cooperado (
  id bigint(5) primary key AUTO_INCREMENT,
  registro varchar(20) NOT NULL,
  codigo varchar(20),
  data date,
  ativo boolean default true,
  anotacao varchar(200),
  tipo_recebimento varchar(20),
  id_medico bigint(5),
  foreign key (id_medico) references pessoa(id)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8; 