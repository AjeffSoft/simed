create table diretoria (
	id bigint(5) primary key auto_increment,
    data_final date,
    chapa varchar(10),
    vencedor boolean default false,
    descricao varchar(200),
    ativo boolean default false,
    anotacao varchar(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;   