Simed = Simed || {};

Simed.Autorizar = (function (){
	
	function Autorizar (){
		this.btn = $('.js-cancelar-btn');
	}
	
	Autorizar.prototype.iniciar = function (){
		this.btn.on('click', onBotaoClicado.bind(this));
	}
	
	function onBotaoClicado(event){
		var botaoClicado = $(event.currentTarget);
		
		var codigo = botaoClicado.data('codigo');
		
		$.ajax({
			url: '/simed/financeiro/imposto/cancelarGerar',
			method: 'PUT',
			data: {
				id: codigo
			},
			success: function(){
				window.location.reload();
			}
		});
	}
	
	return Autorizar;
}());

$(function(){
	var autorizar = new Simed.Autorizar();
	autorizar.iniciar();
}); 