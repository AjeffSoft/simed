Sinte = Sinte || {};

Sinte.CancelarAutorizar = (function (){
	
	function CancelarAutorizar (){
		this.btn = $('.js-cancelar-btn');
	}
	
	CancelarAutorizar.prototype.iniciar = function (){
		this.btn.on('click', onBotaoClicado.bind(this));
	}
	
	function onBotaoClicado(event){
		var botaoClicado = $(event.currentTarget);
		
		var codigo = botaoClicado.data('codigo');
		
		$.ajax({
			url: '/simed/financeiro/contaPagar/cancelarAutorizar',
			method: 'PUT',
			data: {
				id: codigo
			},
			success: function(){
				window.location.reload();
			}
		});
	}
	
	return CancelarAutorizar;
}());

$(function(){
	var cancelarAutorizar = new Sinte.CancelarAutorizar();
	cancelarAutorizar.iniciar();
}); 