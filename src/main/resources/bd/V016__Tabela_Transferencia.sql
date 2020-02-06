CREATE TABLE transferencia_conta (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  observacao varchar(60),
  data date,
  tipo varchar(20),
  status varchar(20),
  valor decimal(10,2),
  id_conta_origem bigint(20),
  id_conta_destino bigint(20),
  id_movimentacao_item bigint(20),
  fechado boolean,
  FOREIGN KEY (id_conta_origem) REFERENCES conta_empresa (id),
  FOREIGN KEY (id_conta_destino) REFERENCES conta_empresa (id),
  FOREIGN KEY (id_movimentacao_item) REFERENCES movimentacao_item (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8