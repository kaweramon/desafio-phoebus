package br.com.phoebus.desafiostarwars.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.phoebus.desafiostarwars.model.Localizacao;
import br.com.phoebus.desafiostarwars.model.NegociarItensContext;
import br.com.phoebus.desafiostarwars.model.Rebelde;
import br.com.phoebus.desafiostarwars.model.Relatorio;
import br.com.phoebus.desafiostarwars.service.RebeldeService;
import br.com.phoebus.desafiostarwars.util.UrlConstants;

@RestController
@RequestMapping(UrlConstants.URL_REBELDE)
public class RebeldeResource {

	@Autowired
	private RebeldeService service;
	
	@PostMapping
	public Rebelde criar(@Valid @RequestBody Rebelde rebelde) {
		return service.criar(rebelde);
	}
	
	@PutMapping(UrlConstants.REBELDE_PARAM_ID + UrlConstants.URL_LOCALIZACAO)
	@ResponseStatus(HttpStatus.OK)
	public void atualizarLocalizacao(@PathVariable Long rebeldeId, @RequestBody Localizacao localizacao) {
		service.atualizarLocalizacao(rebeldeId, localizacao);
	}
	
	@GetMapping
	public List<Rebelde> listar() {
		return service.listar();
	}
	
	@GetMapping(UrlConstants.REBELDE_PARAM_ID)
	public Rebelde recuperarPorId(@PathVariable Long rebeldeId) {
		return service.recuperarPorId(rebeldeId);
	}
	
	@PostMapping(UrlConstants.REBELDE_PARAM_ID + UrlConstants.URL_REPORTAR)
	@ResponseStatus(HttpStatus.OK)
	public void reportar(@PathVariable Long rebeldeId) {
		service.reportar(rebeldeId);
	}
	
	@PostMapping(UrlConstants.URL_NEGOCIAR)
	public void negociar(@RequestParam Long rebelde1Id, @RequestParam Long rebelde2Id,
			@RequestBody NegociarItensContext negociarItens) {
		service.negociar(rebelde1Id, rebelde2Id, negociarItens.getItensRebelde1(), 
				negociarItens.getItensRebelde2());
	}
	
	@GetMapping(UrlConstants.URL_RELATORIO)
	public Relatorio relatorio() {
		return service.obterRelatorio();
	}
	
}
