CREATE TABLE movimentacao (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  data_inicio date NOT NULL,
  data_final date NOT NULL,
  data_fechamento date,
  saldo_inicial decimal(10,2),
  total_creditos decimal(10,2),
  total_debitos decimal(10,2),
  saldo_movimento decimal(10,2),
  saldo_geral decimal(10,2),
  fechado boolean,
  id_empresa bigint(10),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE movimentacao_item (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  saldo_inicial decimal(10,2),
  creditos decimal(10,2),
  debitos decimal(10,2),
  saldo_movimento decimal(10,2),
  saldo_geral decimal(10,2),
  cheque_pendente decimal(10,2),
  id_movimentacao bigint(20),
  id_conta_empresa bigint(20),
  FOREIGN KEY (id_conta_empresa) REFERENCES conta_empresa (id),
  FOREIGN KEY (id_movimentacao) REFERENCES movimentacao (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8