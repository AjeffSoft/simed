CREATE TABLE imposto (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(60),
  apuracao date,
  vencimento date,
  valor decimal(10,2),
  multa decimal(10,2),
  juros decimal(10,2),
  total decimal(10,2),
  valor_nf decimal(10,2),
  numero_nf varchar(15),
  status varchar(20),
  emissao_nf date,
  historico varchar(200),
  id_conta_pagar bigint(20),
  id_conta_pagar_gerada bigint(20),
  FOREIGN KEY (id_conta_pagar) REFERENCES conta_pagar (id),
  FOREIGN KEY (id_conta_pagar_gerada) REFERENCES conta_pagar (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8