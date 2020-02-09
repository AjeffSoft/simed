CREATE TABLE pagamento (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  documento varchar(20),
  data date,
  data_pago date,
  tipo varchar(20),
  valor decimal(10,2),
  situacao boolean,
  status varchar(20),
  id_conta_empresa bigint(20),
  fechado boolean,
  id_movimentacao_item bigint(20),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id),
  FOREIGN KEY (id_conta_empresa) REFERENCES conta_empresa (id),
  FOREIGN KEY (id_movimentacao_item) REFERENCES movimentacao_item (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8