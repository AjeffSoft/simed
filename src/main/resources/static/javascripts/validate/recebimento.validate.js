$("#form").validate({
    rules : {
        data : {
            required : true,
            minlength : 10

        },
		valor : {
		    required : true
		},
		contaEmpresa : {
		    required : true
		}
    },
    messages : {
        data : {
            required : "Informe uma data",
            minlength : "Data inválida"
        },
	    valor : {
	        required : "Informe o valor"
	    },
	    contaEmpresa : {
	        required : "Informe a conta bancária"
	    }
    }
});