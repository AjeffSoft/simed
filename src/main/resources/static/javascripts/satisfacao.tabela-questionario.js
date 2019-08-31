var Simed = Simed || {};

Simed.TabelaQuestionario = (function() {
	
	function TabelaQuestionario() {
		this.Uuid = $('#uuid');
		this.btnIncluir = $('.btn-incluir-questionario');
	}
	
	TabelaQuestionario.prototype.iniciar = function() {
		this.btnIncluir.on('click', onBotaoClicado.bind(this));

	}
	
	
	function onSelecionado (){
		var idPergunta = $(this).find("#perguntaQuestao").val();
		var nota = $('.js-notas:checked').val();
		console.log("Pergunta: ", idPergunta);
		console.log("Nota: ", nota);			
		
		
	}

	function onBotaoClicado(event){
		var uuid = this.Uuid.val().trim();
		
		var questoes = $('.questionario');
		
		questoes.each(function(i, questao){
			var primeiro = questoes[i];
			var nota = $(primeiro).find(".radios").data("value");
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
	
	var tabelaQuestionario = new Simed.TabelaQuestionario();
	tabelaQuestionario.iniciar();
	
});