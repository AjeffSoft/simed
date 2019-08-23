var Sinte = Sinte || {};

Sinte.ComboEmpresa = (function() {

	function ComboEmpresa() {
		this.combo = $('#empresa');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	ComboEmpresa.prototype.iniciar = function() {
		this.combo.on('change', onEmpresaAlterado.bind(this));
	}

	function onEmpresaAlterado() {
		this.emitter.trigger('alterado', this.combo.val());
	}

	return ComboEmpresa;

}());

Sinte.ComboConta = (function() {

	function ComboConta(comboEmpresa) {
		this.comboEmpresa = comboEmpresa;
		this.combo = $('#conta');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenContaSelecionada = $('#inputHiddenContaSelecionada');
	}

	ComboConta.prototype.iniciar = function() {
		reset.call(this);
		this.comboEmpresa.on('alterado', onEmpresaAlterado.bind(this));
		var codigoEmpresa = this.comboEmpresa.combo.val();
		inicializarContas.call(this, codigoEmpresa);
	}

	function onEmpresaAlterado(evento, codigoEmpresa) {
		this.inputHiddenContaSelecionada.val('');
		inicializarContas.call(this, codigoEmpresa);
		inicializarMovimentos.call(this, codigoEmpresa);

	}

	function inicializarContas(codigoEmpresa) {
		if (codigoEmpresa) {
			var resposta = $.ajax({
				url : this.combo.data('url'),
				method : 'GET',
				contentType : 'application/json',
				data : {
					'empresa' : codigoEmpresa
				},
				beforeSend : iniciarRequisicao.bind(this),
				complete : finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarContasFinalizado.bind(this));
		} else {
			reset.call(this);
		}
	}

	
	function inicializarMovimentos(codigoEmpresa) {
		if (codigoEmpresa) {
			var resposta = $.ajax({
				url : this.combo.data('url'),
				method : 'GET',
				contentType : 'application/json',
				data : {
					'empresa' : codigoEmpresa
				},
				complete : finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarMovFinalizado.bind(this));
		} else {
			reset.call(this);
		}
	}	
	
	function onBuscarContasFinalizado(contas) {
		var options = [];
		contas.forEach(function(conta) {
			options.push('<option value="' + conta.id + '">' +conta.nome+ " - CONTA " + conta.tipo + "  Nº " + conta.conta + "  -  " + conta.agencia.banco.nome
					+ '</option>');
		});

		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');

		var codigoContaSelecionada = this.inputHiddenContaSelecionada.val();
		if (codigoContaSelecionada) {
			this.combo.val(codigoContaSelecionada);
		}
	}

//	function onBuscarMovFinalizado(movimentos) {
//		var options = [];
//		movimentos.forEach(function(movimento) {
//			options.push('<option value="' + movimento.id + '">' + "CONTA " + movimento.id + "  Nº " + movimento.id + "  -  " + movimento.id
//					+ '</option>');
//		});
//
//		this.combo.html(options.join(''));
//		this.combo.removeAttr('disabled');
//
//		var codigoMovSelecionada = this.inputHiddenMovSelecionada.val();
//		if (codigoMovSelecionada) {
//			this.combo.val(codigoMovSelecionada);
//		}
//	}
	
	
	
	function reset() {
		this.combo.html('<option value="">Selecione uma conta bancária</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}

	function iniciarRequisicao() {
		reset.call(this);
		this.imgLoading.show();
	}

	function finalizarRequisicao() {
		this.imgLoading.hide();
	}

	return ComboConta;

}());

$(function() {

	var comboEmpresa = new Sinte.ComboEmpresa();
	comboEmpresa.iniciar();

	var comboConta = new Sinte.ComboConta(comboEmpresa);
	comboConta.iniciar();

});
