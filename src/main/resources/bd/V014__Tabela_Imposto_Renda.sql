CREATE TABLE tabela_ir_pf (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(20),
  valor_inicial decimal(10,2),
  valor_final decimal(10,2),
  aliquota decimal(10,2),
  deducao decimal(10,2),
  dependente decimal(10,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE tabela_ir_pj (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  nome varchar(20),
  aliquota_ir decimal(10,2),
  aliquota_pis decimal(10,2),
  aliquota_cofins decimal(10,2),
  aliquota_csll decimal(10,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8