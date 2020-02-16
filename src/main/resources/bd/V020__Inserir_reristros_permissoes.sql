drop table grupo_permissao;
drop table permissao;

CREATE TABLE permissao (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE grupo_permissao (
    id_grupo BIGINT(20) NOT NULL,
    id_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY (id_grupo, id_permissao),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id),
    FOREIGN KEY (id_permissao) REFERENCES permissao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO permissao (nome) VALUES 
('COOPERADOS'),
('FINANCEIRO'),
('GERAL'),
('PESQUISA_SATISFACAO'),
('FINANCEIRO_MOVIMENTACAO_PESQUISAR'),
('FINANCEIRO_MOVIMENTACAO_CADASTRAR'),
('FINANCEIRO_MOVIMENTACAO_EXCLUIR'),
('FINANCEIRO_MOVIMENTACAO_BAIXAR'),
('FINANCEIRO_MOVIMENTACAO_CANCELAR_BAIXA'),
('FINANCEIRO_MOVIMENTACAO_DETALHAR'),
('FINANCEIRO_EXTRATO_PESQUISAR'),
('FINANCEIRO_PLANOCONTA_PESQUISAR'),
('FINANCEIRO_PLANOCONTA_CADASTRAR'),
('FINANCEIRO_PLANOCONTA_EXCLUIR'),
('FINANCEIRO_PLANOCONTA_ALTERAR'),
('FINANCEIRO_PLANOCONTA_DETALHAR'),
('FINANCEIRO_PLANOCONTA_SUBCONTA'),
('FINANCEIRO_CONTARECEBER_PESQUISAR'),
('FINANCEIRO_CONTARECEBER_CADASTRAR'),
('FINANCEIRO_CONTARECEBER_EXCLUIR'),
('FINANCEIRO_CONTARECEBER_ALTERAR'),
('FINANCEIRO_CONTARECEBER_DETALHAR'),
('FINANCEIRO_CONTARECEBER_RECEBER'),
('FINANCEIRO_CONTAPAGAR_PESQUISAR'),
('FINANCEIRO_CONTAPAGAR_CADASTRAR'),
('FINANCEIRO_CONTAPAGAR_EXCLUIR'),
('FINANCEIRO_CONTAPAGAR_ALTERAR'),
('FINANCEIRO_CONTAPAGAR_DETALHAR'),
('FINANCEIRO_CONTAPAGAR_PENDENCIA'),
('FINANCEIRO_AUTORIZARCONTA_PESQUISAR'),
('FINANCEIRO_IMPOSTO_PESQUISAR'),
('FINANCEIRO_IMPOSTO_EXCLUIR'),
('FINANCEIRO_IMPOSTO_GERAR'),
('FINANCEIRO_IMPOSTO_CANCELAR_GERAR'),
('FINANCEIRO_IMPOSTO_DETALHAR'),
('FINANCEIRO_FORNECEDOR_PESQUISAR'),
('FINANCEIRO_FORNECEDOR_CADASTRAR'),
('FINANCEIRO_FORNECEDOR_EXCLUIR'),
('FINANCEIRO_FORNECEDOR_ALTERAR'),
('FINANCEIRO_FORNECEDOR_DETALHAR'),
('FINANCEIRO_AUTORIZARCONTA_PESQUISAR'),
('FINANCEIRO_PAGAMENTO_PESQUISAR'),
('FINANCEIRO_PAGAMENTO_CADASTRAR'),
('FINANCEIRO_PAGAMENTO_EXCLUIR'),
('FINANCEIRO_PAGAMENTO_ALTERAR'),
('FINANCEIRO_PAGAMENTO_DETALHAR'),
('FINANCEIRO_PAGAMENTO_BAIXAR'),
('FINANCEIRO_PAGAMENTO_CANCELAR_BAIXA'),
('FINANCEIRO_TRANSFERENCIA_PESQUISAR'),
('FINANCEIRO_TRANSFERENCIA_CADASTRAR'),
('FINANCEIRO_TRANSFERENCIA_EXCLUIR'),
('FINANCEIRO_TRANSFERENCIA_DETALHAR'),
('FINANCEIRO_TRANSFERENCIA_BAIXAR'),
('FINANCEIRO_TRANSFERENCIA_CANCELAR_BAIXA'),
('FINANCEIRO_AGENCIA_PESQUISAR'),
('FINANCEIRO_AGENCIA_CADASTRAR'),
('FINANCEIRO_AGENCIA_EXCLUIR'),
('FINANCEIRO_AGENCIA_ALTERAR'),
('FINANCEIRO_BANCO_PESQUISAR'),
('FINANCEIRO_BANCO_CADASTRAR'),
('FINANCEIRO_BANCO_EXCLUIR'),
('FINANCEIRO_BANCO_ALTERAR'),
('GERAL_EMPRESA_PESQUISAR'),
('GERAL_EMPRESA_CADASTRAR'),
('GERAL_EMPRESA_EXCLUIR'),
('GERAL_EMPRESA_ALTERAR'),
('GERAL_EMPRESA_DETALHAR'),
('GERAL_EMPRESA_CONTAS_BANCARIAS'),
('GERAL_USUARIO_PESQUISAR'),
('GERAL_USUARIO_CADASTRAR'),
('GERAL_USUARIO_EXCLUIR'),
('GERAL_USUARIO_ALTERAR'),
('GERAL_GRUPO_PESQUISAR'),
('GERAL_GRUPO_CADASTRAR'),
('GERAL_GRUPO_EXCLUIR'),
('GERAL_GRUPO_ALTERAR'),
('GERAL_PERMISSAO_PESQUISAR'),
('GERAL_PERMISSAO_CADASTRAR'),
('GERAL_PERMISSAO_EXCLUIR'),
('GERAL_PERMISSAO_ALTERAR');


INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '1');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '2');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '3');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '4');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '5');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '6');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '7');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '8');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '9');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '10');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '11');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '12');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '13');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '14');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '15');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '16');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '17');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '18');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '19');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '20');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '21');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '22');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '23');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '24');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '25');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '26');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '27');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '28');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '29');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '30');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '31');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '32');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '33');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '34');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '35');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '36');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '37');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '38');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '39');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '40');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '41');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '42');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '43');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '44');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '45');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '46');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '47');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '48');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '49');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '50');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '51');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '52');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '53');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '54');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '55');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '56');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '57');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '58');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '59');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '60');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '61');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '62');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '63');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '64');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '65');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '66');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '67');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '68');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '69');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '70');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '71');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '72');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '73');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '74');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '75');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '76');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '77');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '78');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '79');
INSERT INTO `simed`.`grupo_permissao` (`id_grupo`, `id_permissao`) VALUES ('1', '80');

