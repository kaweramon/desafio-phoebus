package br.com.phoebus.desafiostarwars.service;

import java.util.List;

import br.com.phoebus.desafiostarwars.model.Item;
import br.com.phoebus.desafiostarwars.model.Localizacao;
import br.com.phoebus.desafiostarwars.model.Rebelde;
import br.com.phoebus.desafiostarwars.model.Relatorio;

public interface RebeldeService {

	public Rebelde criar(Rebelde rebelde);
	
	public Rebelde atualizar(Long rebeldeId, Rebelde rebelde);
	
	public void atualizarLocalizacao(Long rebeldeId, Localizacao localizacao);
	
	public List<Rebelde> listar();
	
	public Rebelde recuperarPorId(Long rebeldeId);

	public void reportar(Long rebeldeId);
	
	public void negociar(Long rebelde1Id, Long rebelde2Id, List<Item> itensRebelde1, List<Item> itensRebelde2);
	
	public Relatorio obterRelatorio();
}
