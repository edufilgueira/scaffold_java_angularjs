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

import com.teste.model.Cliente;
import com.teste.service.ClienteService;

@RestController
public class ClienteController {

	@Autowired
	ClienteService clienteService;
	
	
	@RequestMapping(method=RequestMethod.POST, value="/cliente/salvar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cliente>salvar(@RequestBody Cliente cliente) {

		Cliente alterado = clienteService.salvar(cliente);
		return new ResponseEntity<Cliente>(alterado, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cliente/buscartodos", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Cliente>> buscarTodos() {
		
		return new ResponseEntity<>(clienteService.buscarTodos(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/cliente/excluir/{id}")
	public ResponseEntity<String> excluir(@PathVariable Integer id) {
		
		Cliente cliente = clienteService.buscarPorId(id);
		clienteService.excluir(cliente);
		if(cliente == null)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else
			return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/cliente/alterar", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cliente> alterar(@RequestBody Cliente cliente) {
		
		return new ResponseEntity<>(clienteService.salvar(cliente), HttpStatus.OK);
	}
}
