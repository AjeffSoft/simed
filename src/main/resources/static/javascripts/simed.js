var Sinte = Sinte || {};

Sinte.MaskMoney = (function() {
	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({ decimal: ',', thousands: '.' });
		this.plain.maskMoney({ precision: 0, thousands: '.' });
	}
	return MaskMoney;
}());


Sinte.MaskPhoneNumber = (function() {
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone');
	}
	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-0000';
		}
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	return MaskPhoneNumber;
}());


Sinte.MaskCep = (function() {
	function MaskCep() {
		this.inputCep = $('.js-cep');
	}
	MaskCep.prototype.enable = function() {
		this.inputCep.mask('00.000-000');
	}
	return MaskCep;
}());


Sinte.MaskCpfOuCnpj = (function() {
	function MaskCpfOuCnpj() {
		this.input = $('.js-cpfoucnpj');
	}
	MaskCpfOuCnpj.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '999.999.999-99': '99.999.999/9999-99';
		}
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		this.input.mask(maskBehavior, options);
	}
	return MaskCpfOuCnpj;
}());


Sinte.MaskCNPJ = (function(){
	function MaskCNPJ() {
		this.inputCnpj = $('.js-cnpj');
	}
	MaskCNPJ.prototype.enable = function(){
		this.inputCnpj.mask('00.000.000/0000-00');
	}
	return MaskCNPJ;
}());


Sinte.MaskCPF = (function(){
	function MaskCPF() {
		this.inputCpf = $('.js-cpf');
	}
	MaskCPF.prototype.enable = function(){
		this.inputCpf.mask('000.000.000-00');
	}
	return MaskCPF;
}());


Sinte.MaskDate = (function() {
	function MaskDate() {
		this.inputDate = $('.js-date');
	}
	MaskDate.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	return MaskDate;
}());	


Sinte.MaskDateSimples = (function() {
	function MaskDateSimples() {
		this.inputDate = $('.js-dateSimples');
	}
	MaskDateSimples.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
	}
	return MaskDateSimples;
}());	


//Sinte.Security = (function() {
//	
//	function Security() {
//		this.token = $('input[name=_csrf]').val();
//		this.header = $('input[name=_csrf_header]').val();
//	}
//	
//	Security.prototype.enable = function() {
//		$(document).ajaxSend(function(event, jqxhr, settings) {
//			jqxhr.setRequestHeader(this.header, this.token);
//		}.bind(this));
//	}
//	
//	return Security;
//	
//}());


Sinte.MaskCBO = (function(){
	function MaskCBO() {
		this.inputCbo = $('.js-cbo');
	}
	MaskCBO.prototype.enable = function(){
		this.inputCbo.mask('0000-00');
	}
	return MaskCBO;
}());

Sinte.MaskHora = (function(){
	function MaskHora() {
		this.inputHora = $('.js-hora');
	}
	MaskHora.prototype.enable = function(){
		this.inputHora.mask('00:00');
	}
	return MaskHora;
}());


Sinte.MaskRefFerias = (function(){
	function MaskRefFerias() {
		this.inputFerias = $('.js-ferias');
	}
	MaskRefFerias.prototype.enable = function(){
		this.inputFerias.mask('0000/0000');
	}
	return MaskRefFerias;
}());

$(function() {
	var maskMoney = new Sinte.MaskMoney();
	maskMoney.enable();
	
	var maskPhoneNumber = new Sinte.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	var maskCep = new Sinte.MaskCep();
	maskCep.enable();	

	var maskDate = new Sinte.MaskDate();
	maskDate.enable();	

	var maskDateSimples = new Sinte.MaskDateSimples();
	maskDateSimples.enable();	
	
//	var security = new Sinte.Security();
//	security.enable();	

	var maskCnpj = new Sinte.MaskCNPJ();
	maskCnpj.enable();	

	var maskCbo = new Sinte.MaskCBO();
	maskCbo.enable();	

	var maskHora = new Sinte.MaskHora();
	maskHora.enable();	

	var maskRefFerias = new Sinte.MaskRefFerias();
	maskRefFerias.enable();	
	
	var maskCpf = new Sinte.MaskCPF();
	maskCpf.enable();
	
	var maskCpfOuCnpj = new Sinte.MaskCpfOuCnpj();
	maskCpfOuCnpj.enable();	
});