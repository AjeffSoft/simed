var Simed = Simed || {};

Simed.MaskMoney = (function() {
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


Simed.MaskPhoneNumber = (function() {
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone');
	}
	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00)90000-0000' : '(00)0000-0000';
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

Simed.MaskPhoneNumberCel = (function() {
	function MaskPhoneNumberCel() {
		this.inputPhoneNumberCel = $('.js-celular');
	}
	MaskPhoneNumberCel.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00)90000-0000' : '(00)90000-0000';
		}
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		this.inputPhoneNumberCel.mask(maskBehavior, options);
	}
	return MaskPhoneNumberCel;
}());


Simed.MaskCep = (function() {
	function MaskCep() {
		this.inputCep = $('.js-cep');
	}
	MaskCep.prototype.enable = function() {
		this.inputCep.mask('00.000-000');
	}
	return MaskCep;
}());


Simed.MaskCpfOuCnpj = (function() {
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


Simed.MaskCNPJ = (function(){
	function MaskCNPJ() {
		this.inputCnpj = $('.js-cnpj');
	}
	MaskCNPJ.prototype.enable = function(){
		this.inputCnpj.mask('00.000.000/0000-00');
	}
	return MaskCNPJ;
}());


Simed.MaskCPF = (function(){
	function MaskCPF() {
		this.inputCpf = $('.js-cpf');
	}
	MaskCPF.prototype.enable = function(){
		this.inputCpf.mask('000.000.000-00');
	}
	return MaskCPF;
}());


Simed.MaskDate = (function() {
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


Simed.MaskDateHora = (function() {
	function MaskDateHora() {
		this.inputDate = $('.js-date-hora');
	}
	MaskDateHora.prototype.enable = function() {
		this.inputDate.mask('00/00/0000 00:00');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	return MaskDateHora;
}());	

Simed.MaskDateSimples = (function() {
	function MaskDateSimples() {
		this.inputDate = $('.js-dateSimples');
	}
	MaskDateSimples.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
	}
	return MaskDateSimples;
}());	


//Simed.Security = (function() {
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


Simed.MaskCBO = (function(){
	function MaskCBO() {
		this.inputCbo = $('.js-cbo');
	}
	MaskCBO.prototype.enable = function(){
		this.inputCbo.mask('0000-00');
	}
	return MaskCBO;
}());

Simed.MaskHora = (function(){
	function MaskHora() {
		this.inputHora = $('.js-hora');
	}
	MaskHora.prototype.enable = function(){
		this.inputHora.mask('00:00');
	}
	return MaskHora;
}());


Simed.MaskRefFerias = (function(){
	function MaskRefFerias() {
		this.inputFerias = $('.js-ferias');
	}
	MaskRefFerias.prototype.enable = function(){
		this.inputFerias.mask('0000/0000');
	}
	return MaskRefFerias;
}());

$(function() {
	var maskMoney = new Simed.MaskMoney();
	maskMoney.enable();

	var maskPhoneNumber = new Simed.MaskPhoneNumber();
	maskPhoneNumber.enable();

	var maskPhoneNumberCel = new Simed.MaskPhoneNumberCel();
	maskPhoneNumberCel.enable();
	
	var maskCep = new Simed.MaskCep();
	maskCep.enable();	

	var maskDate = new Simed.MaskDate();
	maskDate.enable();	

	var maskDateHora = new Simed.MaskDateHora();
	maskDateHora.enable();	
	
	var maskDateSimples = new Simed.MaskDateSimples();
	maskDateSimples.enable();	
	
//	var security = new Simed.Security();
//	security.enable();	

	var maskCnpj = new Simed.MaskCNPJ();
	maskCnpj.enable();	

	var maskCbo = new Simed.MaskCBO();
	maskCbo.enable();	

	var maskHora = new Simed.MaskHora();
	maskHora.enable();	

	var maskRefFerias = new Simed.MaskRefFerias();
	maskRefFerias.enable();	
	
	var maskCpf = new Simed.MaskCPF();
	maskCpf.enable();
	
	var maskCpfOuCnpj = new Simed.MaskCpfOuCnpj();
	maskCpfOuCnpj.enable();	
});