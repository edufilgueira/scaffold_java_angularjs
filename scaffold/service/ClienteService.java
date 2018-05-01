package com.teste.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.model.Cliente;
import com.teste.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	public Cliente salvar(Cliente cliente) {

		clienteRepository.save(cliente);
		return cliente;
	}
	
	public Collection<Cliente> buscarTodos() {
		return  clienteRepository.findAll();
	}
	
	public void excluir(Cliente cliente) {
		clienteRepository.delete(cliente);
	}
	
	public Cliente buscarPorId(Integer id) {
		return clienteRepository.findOne(id); 
	}
	
}