package com.teste.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Classe {

	@Id
	@SequenceGenerator(name = "classe_seq", sequenceName = "classe_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classe_seq")
	private Integer id;

	private String artigo;

	@JoinColumn(nullable = false)
	@ManyToOne
	private Classe classe;



	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getArtigo() {
		return artigo;
	}
	public void setArtigo(String artigo) {
		this.artigo = artigo;
	}
	public Classe getClasse() {
		return classe;
	}
	public void setClasse(Classe classe) {
		this.classe = classe;
	}
}