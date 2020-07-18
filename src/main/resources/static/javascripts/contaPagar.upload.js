var Simed = Simed || {};

Simed.UploadContaPagar = (function() {
	
	function UploadContaPagar() {
		this.inputNomeContaPagar = $('input[name=upload]');
		this.inputContentType = $('input[name=contentType]');
		
		this.htmlUploadTemplate = $('#upload-contapagar').html();
		this.template = Handlebars.compile(this.htmlUploadTemplate);
		
		this.containerUpload = $('.js-container-upload');
		
		this.uploadDrop = $('#upload-drop');
	}
	
	UploadContaPagar.prototype.iniciar = function () {
		var settings = {
			type: 'json',
			filelimit: 1,
			allow: '*.(pdf)',
			action: '/simed/financeiro/contasPagar/upload',
			complete: onUploadCompleto.bind(this)
		}
		
		UIkit.uploadSelect($('#upload-select'), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);

		if (this.inputNomeContaPagar.val()) {
			onUploadCompleto.call(this, { nome:  this.inputNomeContaPagar.val(), contentType: this.inputContentType.val()});
		}
	}
	
	function onUploadCompleto(resposta) {
		this.inputNomeContaPagar.val(resposta.nome);
		this.inputContentType.val(resposta.contentType);
		
		this.uploadDrop.addClass('hidden');
		var htmlUpload = this.template({nomeUpload: resposta.nome});
		this.containerUpload.append(htmlUpload);
		
		$('.js-remove-upload').on('click', onRemoverContaPagar.bind(this));
	}
	
	function onRemoverContaPagar() {
		$('.js-upload-contapagar').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeContaPagar.val('');
		this.inputContentType.val('');
	}
	
	return UploadContaPagar;
	
})();

$(function() {
	var uploadContaPagar = new Simed.UploadContaPagar();
	uploadContaPagar.iniciar();
});