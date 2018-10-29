package br.com.phoebus.desafiostarwars.model;

public enum ItemTipo {

	ARMA("Arma"), MUNICAO("Minução"), AGUA("Água"), COMIDA("Comida");
	
	public String tipo;
	
	public String getTipo() {
		return this.tipo;
	}
	
	ItemTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
