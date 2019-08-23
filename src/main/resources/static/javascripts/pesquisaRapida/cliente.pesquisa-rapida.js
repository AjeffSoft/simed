Sinte = Sinte || {};

Sinte.PesquisaRapidaFornecedor = (function(){
	
	function PesquisaRapidaFornecedor(){
		this.pesquisaRapidaFornecedorModal = $('#pesquisaRapidaFornecedor');
		this.nomeInput = $('#nomeFornecedorModal');
		this.botaoPesquisar = $('.js-pesquisa-rapida-fornecedor-btn');
		this.divMsgErro = $('.js-mensagem-erro');
		this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaFornecedor');
		this.htmlTabelaPesquisa = $('#tabela-pesquisa-rapida-fornecedor').html();
		this.template = Handlebars.compile(this.htmlTabelaPesquisa);
	}
	
	PesquisaRapidaFornecedor.prototype.iniciar = function(){
		this.pesquisaRapidaFornecedorModal.on('shown.bs.modal', onModalShow.bind(this));
		this.botaoPesquisar.on('click', onPesquisaRapidaClicado.bind(this)); 
	}
	
	function onModalShow(){
		this.nomeInput.focus();
	}
	
	function onPesquisaRapidaClicado(event){
		event.preventDefault();
		$.ajax({
			url: this.pesquisaRapidaFornecedorModal.find('form').attr('action'),
			methos:'GET',
			contentType:'application/json',
			data:{
				nome: this.nomeInput.val()
			},
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisa.bind(this)
		})
	}
	
	function onPesquisaConcluida(result){
		this.divMsgErro.addClass('hidden');

		var html = this.template(result);
		this.containerTabelaPesquisa.html(html);

		var tabelaFornecedorPesquisaRapida = new Sinte.TabelaFornecedorPesquisaRapida(this.pesquisaRapidaFornecedorModal);
		tabelaFornecedorPesquisaRapida.iniciar();
	}
	
	function onErroPesquisa(){
		this.divMsgErro.removeClass('hidden');
	}
	
	return PesquisaRapidaFornecedor;
	
}());



Sinte.TabelaFornecedorPesquisaRapida = (function (){
	
	function TabelaFornecedorPesquisaRapida(modal){
		this.modalFornecedor = modal;
		this.fornecedor = $('.js-fornecedor-pesquisa-rapida');
	}
	
	TabelaFornecedorPesquisaRapida.prototype.iniciar = function(){
		this.fornecedor.on('click', onFornecedorSelecionado.bind(this));
	}
	
	function onFornecedorSelecionado(event){
		this.modalFornecedor.modal('hide');

		var fornecedorSelecionado = $(event.currentTarget);
		
		$('#nomeFornecedor').val(fornecedorSelecionado.data('nome'));
		$('#documentoFornecedor').val(fornecedorSelecionado.data('documento'));
		$('#codigoFornecedor').val(fornecedorSelecionado.data('codigo'));
	}
	
	return TabelaFornecedorPesquisaRapida;
}());


$(function(){
	var pesquisaRapidaFornecedor = new Sinte.PesquisaRapidaFornecedor();
	pesquisaRapidaFornecedor.iniciar();
});