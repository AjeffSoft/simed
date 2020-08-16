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
		data: {valor: valor, idFornecedor: idFornecedor}
	}).success(function(e){
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
	$.ajax({
		mothod: 'get',
		url: '/simed/financeiro/imposto/getIrrf',
		contentType: 'application/json',
		data: {valor: valor, idFornecedor: idFornecedor}
	}).success(function(e){
		setarImposto('#linhaIrrf', e);
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