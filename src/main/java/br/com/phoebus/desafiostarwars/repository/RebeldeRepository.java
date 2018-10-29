package br.com.phoebus.desafiostarwars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.phoebus.desafiostarwars.model.Rebelde;

public interface RebeldeRepository extends JpaRepository<Rebelde, Long> {

}
