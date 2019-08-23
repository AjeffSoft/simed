$("#form").validate({
    rules : {
        conta : {
            required : true,
            minlength : 2
        },
		tipo : {
		    required : true
		},
		agencia : {
		    required : true
		}
    },
    messages : {
        conta : {
            required : "Informe a conta",
            minlength : "Mínimo de 2 caracteres."
        },
	    tipo : {
	        required : "Informe o tipo da conta"
	    },
	    agencia : {
	        required : "Informe a agencia"
	    }
    }
});