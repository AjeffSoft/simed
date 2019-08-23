package com.ajeff.simed.financeiro.controller;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.async.DeferredResult;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.ajeff.simed.financeiro.dto.UploadContaPagarDTO;
//import com.ajeff.simed.financeiro.model.UploadContaPagar;
//import com.ajeff.simed.financeiro.service.UploadContaPagarService;
//import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
//import com.ajeff.simed.financeiro.storage.UploadContaPagarStorageRunnable;
//import com.ajeff.simed.financeiro.storage.UploadStorage;
//
//@RestController
//@RequestMapping("/financeiro/uploadContaPagar")
public class UploadContaPagarController {
//
//
//	@Autowired
//	private UploadContaPagarService service;
//	@Autowired
//	private UploadStorage uploadStorage;
//	
//	@PostMapping
//	public DeferredResult<UploadContaPagarDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
//		DeferredResult<UploadContaPagarDTO> resultado = new DeferredResult<>();
//		
//		Thread thread = new Thread(new UploadContaPagarStorageRunnable(files, resultado, uploadStorage));
//		thread.start();
//		
//		return resultado;
//	}
//	
//	
//	@GetMapping("/upload/{nome:.*}")
//	public byte[] exibirUpload(@PathVariable String nome) {
//		return uploadStorage.exibirUpload(nome);
//	}
//
//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") UploadContaPagar uploadPagar){
//
//		try {
//			service.excluir(uploadPagar);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
//	
//	
//	

}
