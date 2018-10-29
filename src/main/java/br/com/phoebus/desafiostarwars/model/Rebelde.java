package br.com.phoebus.desafiostarwars.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "rebelde")
@Data
@EqualsAndHashCode(of = "id")
public class Rebelde {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 5, max = 50)
	private String nome;
	
	@NotNull
	private int idade;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Genero genero;
	
	private int reportCont;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Localizacao localizacao;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Inventario inventario;
	
}
