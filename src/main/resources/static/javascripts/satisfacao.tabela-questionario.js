var Sinte = Sinte || {};

Sinte.TabelaQuestionario = (function() {
	
	function TabelaQuestionario() {
		this.inputPergunta = $('.js-pergunta');
//		this.inputNota = $('.js-nota');
		this.Uuid = $('#uuid');
		this.btnIncluir = $('.btn-incluir-questionario');
	}
	
	TabelaQuestionario.prototype.iniciar = function() {
		this.btnIncluir.on('click', onBotaoClicado.bind(this));
	}
	

	function onBotaoClicado(event){
		var uuid = this.Uuid.val().trim();
		
		var questoes = $('.questionario');
		var nota1 = $(".radio1").val();
		console.log(nota1);
		
		questoes.each(function(i, questao){
			var primeiro = questoes[i];
			var nota = $(primeiro).find(".radio").data("value");

			console.log(primeiro);

			console.log(nota);
//			var botaoClicado = $(event.currentTarget);
//			var idPergunta = botaoClicado.data('pergunta');
			var idPergunta = $(primeiro).find("#pergunta").val();

			console.log(idPergunta);
			
			var resposta = $.ajax({
				url: 'questao',
				method: 'POST',
				data: {
					idPergunta: idPergunta, nota: nota, uuid: uuid
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
	
	var tabelaQuestionario = new Sinte.TabelaQuestionario();
	tabelaQuestionario.iniciar();
	
});