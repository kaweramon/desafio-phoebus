package br.com.phoebus.desafiostarwars.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity(name = "localizacao")
@Data
public class Localizacao {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private float longitude;
	
	private float latitude;
	
	private String nomeGalaxia;
	
}
