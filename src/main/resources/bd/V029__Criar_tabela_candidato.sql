create table candidato (
	id bigint(5) primary key auto_increment,
    tipo_diretor varchar(20),
    cargo_diretor varchar(20),
    anotacao varchar(200),
    id_cooperado bigint(5),
    foreign key (id_cooperado) references cooperado(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;   