alter table candidato add id_diretoria bigint(5);
alter table candidato add foreign key (id_diretoria) references diretoria(id);

alter table assembleia add id_diretoria bigint(5);
alter table assembleia add foreign key (id_diretoria) references diretoria(id);