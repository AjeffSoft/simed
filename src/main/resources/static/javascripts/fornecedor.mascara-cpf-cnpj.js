var Simed = Simed || {};

Simed.MascaraCpfCnpj = (function(){
	
	function MascaraCpfCnpj(){
		this.radioTipo = $('.js-radio-tipo');
		this.labelCpfCnpj = $('[for=cpfOuCnpj]');
		this.inputCpfCnpj = $('#cpfOuCnpj');
		this.labelPisOuIE = $('[for=pisOuIE]');
		this.inputPisOuIE = $('#pisOuIE');
	}
	
	MascaraCpfCnpj.prototype.iniciar = function(){
		this.radioTipo.on('change', onTipoAlterado.bind(this));
		var tipoFornecedorSelecionado = this.radioTipo.filter(':checked')[0];
		
		if(tipoFornecedorSelecionado){
			aplicarMascara.call(this, $(tipoFornecedorSelecionado));
		}
	}
	
	function onTipoAlterado(evento){
		var tipoSelecionado = $(evento.currentTarget);
		this.inputCpfCnpj.val('');
		aplicarMascara.call(this, tipoSelecionado);
	}
	
	function aplicarMascara(tipoSelecionado){
		this.labelCpfCnpj.text(tipoSelecionado.data('documento'));
		this.inputCpfCnpj.mask(tipoSelecionado.data('mascara'));
		this.inputCpfCnpj.removeAttr('disabled');
		this.inputPisOuIE.removeAttr('disabled');
	}
	
	return MascaraCpfCnpj;
}());

$(function(){
	var mascaraCpfCnpj = new Simed.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
});