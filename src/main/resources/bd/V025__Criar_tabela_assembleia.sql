create table assembleia (
	id bigint(5) primary key auto_increment,
    data date,
    tipo_assembleia varchar(15), 
    descricao varchar(100),
    anexo varchar(200),
    eleicao boolean default false,
    resumo text(5000)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8   