$("#form").validate({
    rules : {
        nome : {
            required : true,
            minlength : 3
        },
		tipo : {
		    required : true
		}
    },
    messages : {
        nome : {
            required : "Informe o nome",
            minlength : "Mínimo de 3 caracteres."
        },
	    tipo : {
	        required : "Informe o tipo da conta"
	    }
    }
});