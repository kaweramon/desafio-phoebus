package br.com.phoebus.desafiostarwars.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "item")
@Data
@NoArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private ItemTipo nome;

	@NotNull
	private int quantidade;
	
	public Item(ItemTipo nome, int quantidade) {
		this.nome = nome;
		this.quantidade = quantidade;
	}
	
}
