CREATE TABLE conta_receber (
  id bigint(20) PRIMARY KEY AUTO_INCREMENT,
  historico varchar(200) DEFAULT NULL,
  data_emissao date DEFAULT NULL,
  vencimento date NOT NULL,
  valor decimal(10,2) NOT NULL,
  valor_recebido decimal(10,2),
  documento varchar(20),
  status varchar(15),
  parcela bigint(20),
  total_parcela bigint(20)L,
  id_conta_secundaria bigint(20),
  id_empresa bigint(20),
  id_fornecedor bigint(20),
  FOREIGN KEY (id_conta_secundaria) REFERENCES plano_conta_secundaria (id),
  FOREIGN KEY (id_empresa) REFERENCES empresa (id),
  FOREIGN KEY (id_fornecedor) REFERENCES fornecedor (id)
) ENGINE=InnoDB AUTO_INCREMENT=1493 DEFAULT CHARSET=utf8