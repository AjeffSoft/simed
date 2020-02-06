CREATE TABLE recebimento (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  data date,
  valor decimal(10,2),
  id_conta_empresa bigint(20),
  id_conta_receber bigint(20),
  id_movimentacao_item bigint(20),
  fechado boolean,
  FOREIGN KEY (id_conta_empresa) REFERENCES conta_empresa (id),
  FOREIGN KEY (id_movimentacao_item) REFERENCES movimentacao_item (id),
  FOREIGN KEY (id_conta_receber) REFERENCES conta_receber (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8