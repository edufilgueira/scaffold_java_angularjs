package com.teste.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.model.Classe;
import com.teste.service.ClasseService;

@RestController
public class ClasseController {

	@Autowired
	ClasseService classeService;
	
	
	@RequestMapping(method=RequestMethod.POST, value="/classe/salvar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Classe>salvar(@RequestBody Classe classe) {

		Classe alterado = classeService.salvar(classe);
		return new ResponseEntity<Classe>(alterado, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/classe/buscartodos", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Classe>> buscarTodos() {
		
		return new ResponseEntity<>(classeService.buscarTodos(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/classe/excluir/{id}")
	public ResponseEntity<String> excluir(@PathVariable Integer id) {
		
		Classe classe = classeService.buscarPorId(id);
		classeService.excluir(classe);
		if(classe == null)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else
			return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/classe/alterar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Classe> alterar(@RequestBody Classe classe) {
		
		return new ResponseEntity<>(classeService.salvar(classe), HttpStatus.OK);
	}
}
