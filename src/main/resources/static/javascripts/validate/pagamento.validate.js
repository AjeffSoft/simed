$("#form").validate({
    rules : {
        data : {
            required : true,
            minlength : 10

        },
		tipo : {
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
	    tipo : {
	        required : "Informe o tipo"
	    },
	    contaEmpresa : {
	        required : "Informe a conta bancária"
	    }
    }
});