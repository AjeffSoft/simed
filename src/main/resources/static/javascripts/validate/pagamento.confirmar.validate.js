$("#form").validate({
    rules : {
        dataPago : {
            required : true,
            minlength : 10

        }
    },
    messages : {
        dataPago : {
            required : "Informe uma data",
            minlength : "Data inválida"
        }
    }
});