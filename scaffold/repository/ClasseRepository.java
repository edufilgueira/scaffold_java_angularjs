package com.teste.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teste.model.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer>{

}
