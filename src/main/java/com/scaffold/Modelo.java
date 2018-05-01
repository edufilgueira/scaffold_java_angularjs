package com.scaffold;

import java.util.Map;

public class Modelo {
	private String pacote;
	private String modelo;
	private Map<String, String> atributos;
	
	public String getPacote() {
		return pacote;
	}
	public void setPacote(String pacote) {
		this.pacote = pacote;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public Map<String, String> getAtributos() {
		return atributos;
	}
	public void setAtributos(Map<String, String> atributos) {
		this.atributos = atributos;
	}
	
	

}
