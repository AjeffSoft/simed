function formatarValor(valor){
	if (valor != 0 || valor != ''){
	  valor = valor.replace(".","");
	  valor = valor.replace(",",".");
	}

	return parseFloat(valor);
}


function getIss(valor, aliquotaIss){
	if(aliquotaIss !== "0,00" && valor !== "0,00"){
		
		$.ajax({
			method: "get",
			contentType: 'application/json',
			url: "/simed/financeiro/imposto/getISS",
			data: {aliquota: aliquotaIss, valor : valor}
		}).success(function(e){
			setarImposto('#linhaISS', e);
		});
	}else {
		zerarImposto('#linhaISS');
	}
	
};

function getInss(valor, idFornecedor){
	$.ajax({
		mothod: 'get',
		url: '/simed/financeiro/imposto/getInss',
		contentType: 'application/json',
		data: {valor: valor, idFornecedor: idFornecedor},
	}).success(function(e){
		if($('#marqReterIrrf').val() === 'true'){
			getIrrf(valor, idFornecedor);
		}
		setarImposto('#linhaInss', e);
	})
};

function getPccs(valor, idFornecedor){
	$.ajax({
		mothod: 'get',
		url: '/simed/financeiro/imposto/getPccs',
		contentType: 'application/json',
		data: {valor: valor, idFornecedor: idFornecedor}
	}).success(function(e){
		setarImposto('#linhaPccs', e);

	})	
};

function getIrrf(valor, idFornecedor){
	var inss = "N";
	if($('#marqReterINSS').val() === 'S'){
		inss = "S"
	}
	$.ajax({
		mothod: 'get',
		url: '/simed/financeiro/imposto/getIrrf',
		contentType: 'application/json',
		data: {valor: valor, idFornecedor: idFornecedor, inss: inss}
	}).success(function(e){
		setarImposto('#linhaIrrf', e);
		console.log(e);

	})	
};

function zerarImposto(idCampo){
	$(idCampo).empty();
	$(idCampo).append("0,00");			
}

function setarImposto(idCampo, e){
	$(idCampo).empty();
	$(idCampo).append(e);	
}

function resetarCampos(){
	zerarImposto('#linhaIrrf');
	zerarImposto('#linhaPccs');
	zerarImposto('#linhaISS');
	zerarImposto('#linhaInss');
	
	$('select#marqReterINSS').val('');
	$('select#marqReterPCCS').val('');
	$('select#marqReterIrrf').val('');
	$('#issPorcentagem').val('');
}