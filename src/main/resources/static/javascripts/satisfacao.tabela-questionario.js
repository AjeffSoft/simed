var Simed = Simed || {};

Simed.TabelaQuestionario = (function() {
	
	function TabelaQuestionario() {
		this.Uuid = $('#uuid');
		this.btnIncluir = $('.btn-incluir-questionario');
	}
	
	TabelaQuestionario.prototype.iniciar = function() {
		this.btnIncluir.on('click', onSelecionado.bind(this));

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
		

			
		
		
		questoes.each(function(i){

			console.log("Pergunta: ", idPergunta);
			console.log("Nota: ", perg);			
			
			var perg2 = $(questao).find('.js-notas2:checked').val();
			console.log("Nota: ", perg2);			
			//
//			console.log(nota);
//
//			var idPergunta = $(primeiro).find("#pergunta").val();
//
//			console.log(idPergunta);
//			
//			var resposta = $.ajax({
//				url: 'questao',
//				method: 'POST',
//				data: {
//					idPergunta: idPergunta, nota: nota, uuid: uuid
//				}
			});

//		$('.js-notas').each(function(){
//			if($(this).is(':checked'))
//				var nota = $(this).val();
//			console.log(nota);
//		})
		
		
		
//		});
			

		
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