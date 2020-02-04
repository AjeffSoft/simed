$("#form").validate({
    rules : {
        vencimento : {
            required : true,
        },
		historico : {
		    required : true,
		    minlength : 3
		},
		valor : {
		    required : true
		}
    },
    messages : {
        vencimento : {
            required : "Informe um vencimento"
        },
	    historico : {
	        required : "Informe um histórico",
	        minlength : "Histórico com no minimo 3 caracteres"
	    },
	    valor : {
	        required : "Informe o valor"
	    }
    }
});