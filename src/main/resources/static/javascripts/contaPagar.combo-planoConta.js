var Sinte = Sinte || {};

Sinte.ComboPlanoConta = (function() {
	
	function ComboPlanoConta () {
		this.combo = $('#planoConta');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	ComboPlanoConta.prototype.iniciar = function (){
		this.combo.on('change', onPlanoContaAlterado.bind(this));
	}
	
	function onPlanoContaAlterado(){
		this.emitter.trigger('alterado', this.combo.val());
	}
	
	return ComboPlanoConta;
	
}());


Sinte.ComboSubPlanoConta = (function (){
	
	function ComboSubPlanoConta (comboPlanoConta){
		this.comboPlanoConta = comboPlanoConta;
		this.combo = $('#subPlanoConta');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenSubPlanoContaSelecionada = $('#inputHiddenSubPlanoContaSelecionado');
	}
	
	ComboSubPlanoConta.prototype.iniciar = function (){
		reset.call(this);
		this.comboPlanoConta.on('alterado', onPlanoContaAlterado.bind(this));
		var codigoPlanoConta = this.comboPlanoConta.combo.val();
		inicializarSubPlanoConta.call(this, codigoPlanoConta);
	}
	
	function onPlanoContaAlterado(event, codigoPlanoConta){
		this.inputHiddenSubPlanoContaSelecionada.val('');
		inicializarSubPlanoConta.call(this, codigoPlanoConta);
	}
	
	function inicializarSubPlanoConta(codigoPlanoConta){
		if(codigoPlanoConta){
			var resposta = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: {'planoConta': codigoPlanoConta },
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});
			
			resposta.done(onBuscarSubPlanoContaFinalizado.bind(this));
		}else {
			reset.call(this);
		}	
	}
	
	function onBuscarSubPlanoContaFinalizado(subPlanosContas){
		var options = [];
		subPlanosContas.forEach(function(subPlanoConta){
			options.push('<option value="' + subPlanoConta.id + '">' + subPlanoConta.nome + '</option>');
		});
		
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		var codigoSubContaSelecionada = this.inputHiddenSubPlanoContaSelecionada.val();
		
		if(codigoSubContaSelecionada){
			this.combo.val(codigoSubContaSelecionada);
		}
	}
	
	function reset(){
		this.combo.html('<option value="">Selecione uma conta secundária</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao(){
		reset.call(this);
		this.imgLoading.show();
	}
	
	function finalizarRequisicao(){
		this.imgLoading.hide();
	}
	
	return ComboSubPlanoConta;
	
}());

$(function(){
	
	var comboPlanoConta = new Sinte.ComboPlanoConta();
	comboPlanoConta.iniciar();

	var comboSubPlanoConta = new Sinte.ComboSubPlanoConta(comboPlanoConta);
	comboSubPlanoConta.iniciar();

});