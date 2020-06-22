create table assembleia_presente (
	id_assembleia bigint(5),
    id_presente bigint(5),
    primary key (id_assembleia, id_presente),
    foreign key (id_assembleia) references assembleia(id),
    foreign key (id_presente) references cooperado(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;