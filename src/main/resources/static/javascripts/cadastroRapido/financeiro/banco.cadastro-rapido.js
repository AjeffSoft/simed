var Sinte = Sinte || {}; 

Sinte.BancoCadastroRapido = (function() {
	
	function BancoCadastroRapido() {
		this.modal = $('#modalCadastroRapidoBanco');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-banco-salvar-btn');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
		this.inputCodigo = $('#codigoBanco');
		this.inputNome = $('#nomeBanco');
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-banco');
	}
	
		BancoCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event) { event.preventDefault() });
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		this.modal.on('hide.bs.modal', onModalClose.bind(this))
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
	}
	
	function onModalShow() {
		this.inputCodigo.focus();
	}
	
	function onModalClose() {
		this.inputCodigo.val('');
		this.inputNome.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		var codigoBanco = this.inputCodigo.val().trim();
		var nomeBanco = this.inputNome.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeBanco, codigo: codigoBanco }),
			error: onErroSalvandoBanco.bind(this),
			success: onBancoSalvo.bind(this)
		});
	}
	
	function onErroSalvandoBanco(obj) {
		var mensagemErro = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onBancoSalvo(banco) {
		var comboBanco = $('#banco');
		comboBanco.append('<option value=' + banco.id + '>' + banco.nome + ' - Código: ' + banco.codigo + '</option>');
		comboBanco.val(banco.id);
		this.modal.modal('hide');
	}
	
	return BancoCadastroRapido;
	
}());

$(function() {
	var bancoCadastroRapido = new Sinte.BancoCadastroRapido();
	bancoCadastroRapido.iniciar();
});