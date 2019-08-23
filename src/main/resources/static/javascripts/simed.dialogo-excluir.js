Sinte = Sinte || {};

Sinte.DialogoExcluir = (function (){
	
	function DialogoExluir (){
		this.excluirBtn = $('.js-excluir-btn');
	}
	
	DialogoExluir.prototype.iniciar = function (){
		this.excluirBtn.on('click', onExcluirObjeto.bind(this));
		
		if (window.location.search.indexOf('excluido') > -1) {
			swal('Pronto!', 'Excluído com sucesso!', 'success');
		}		
	} 
	
	function onExcluirObjeto(event){
		event.preventDefault();
		var botaoClicado = $(event.currentTarget);
		var url = botaoClicado.data('url');
		var objeto = botaoClicado.data('objeto');
		
		swal({
			  title: 'Você tem certeza?',
			  text: 'Deseja realmente excluir " '+objeto+' "? \n Se clicar em sim o registro irá ser excluído definitivamente.',
			  type: "warning",
			  showCancelButton: true,
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "Sim, quero excluir!",
			  closeOnConfirm: false
			}, onClicouSim.bind(this, url));		
	}
	
	function onClicouSim(url){
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onExcluidoSucesso.bind(this),
			error: onErroExcluir.bind(this)
		});
	}
	
	function onExcluidoSucesso(){
		var urlAtual = window.location.href;
		var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
		var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
		
		window.location = novaUrl;
	}
	
	function onErroExcluir(e) {
		swal('Oops!', e.responseText, 'error');
	}	
	
	return DialogoExluir;
}());

$(function(){
	var dialogoExcluir = new Sinte.DialogoExcluir();
	dialogoExcluir.iniciar();
}); 