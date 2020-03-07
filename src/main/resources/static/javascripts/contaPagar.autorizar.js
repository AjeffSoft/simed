Simed = Simed || {};

Simed.Autorizar = (function (){
	
	function Autorizar (){
		this.btn = $('.js-autorizar-btn');
	}
	
	Autorizar.prototype.iniciar = function (){
		this.btn.on('click', onBotaoClicado.bind(this));
	}
	
	function onBotaoClicado(event){
		var botaoClicado = $(event.currentTarget);
		var url = botaoClicado.data('url');
		var codigo = botaoClicado.data('codigo');
		
		$.ajax({
			url: url,
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