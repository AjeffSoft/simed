var Simed = Simed || {};

Simed.TabelaQuestionario = (function() {
	
	function TabelaQuestionario() {
		this.Uuid = $('#uuid');
		this.btnIncluir = $('.btn-incluir-questionario');
	}
	
	TabelaQuestionario.prototype.iniciar = function() {
		this.btnIncluir.on('click', onBotaoClicado.bind(this));

	}
	
	
	
	function onBotaoClicado(event){
		event.preventDefault();
		var uuid = this.Uuid.val().trim();
		
		var questoes = $('.questionario');
	
		questoes.each(function(i, questao){
			var pergunta = $(questao).find(".pergunta").val();
			var resposta = $(questao).find("input[type=radio]:checked").val();
			console.log(pergunta, " - ", resposta);
			
			var resposta = $.ajax({
				url: 'questao',
				method: 'POST',
				data: {
					pergunta: pergunta, resposta: resposta, uuid: uuid
				}
			});
		});

		
//		resposta.done(function(data){
//			console.log('retorno ', data);
//		});
	}	
	
	return TabelaQuestionario
	
}());

$(function() {
	
	var tabelaQuestionario = new Simed.TabelaQuestionario();
	tabelaQuestionario.iniciar();
	
});