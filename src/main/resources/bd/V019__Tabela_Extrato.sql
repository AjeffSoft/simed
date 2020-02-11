CREATE TABLE extrato (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  data date,
  tipo varchar(20),
  credito boolean,
  historico varchar(250),
  status varchar(20),
  valor decimal(10,2),
  saldo decimal(10,2),
  id_pagamento bigint(20),
  id_recebimento bigint(20),
  id_transferencia bigint(20),
  id_movimentacao_item bigint(20),
  id_conta_bancaria bigint(20),
  FOREIGN KEY (id_pagamento) REFERENCES pagamento (id),
  FOREIGN KEY (id_transferencia) REFERENCES transferencia_conta (id),
  FOREIGN KEY (id_recebimento) REFERENCES recebimento (id),
  FOREIGN KEY (id_conta_bancaria) REFERENCES conta_empresa (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8