package com.teste.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.model.Classe;
import com.teste.repository.ClasseRepository;

@Service
public class ClasseService {

	@Autowired
	ClasseRepository classeRepository;

	public Classe salvar(Classe classe) {

		classeRepository.save(classe);
		return classe;
	}
	
	public Collection<Classe> buscarTodos() {
		return  classeRepository.findAll();
	}
	
	public void excluir(Classe classe) {
		classeRepository.delete(classe);
	}
	
	public Classe buscarPorId(Integer id) {
		return classeRepository.findOne(id); 
	}
	
}