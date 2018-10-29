package br.com.phoebus.desafiostarwars.service.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import br.com.phoebus.desafiostarwars.exceptionhandler.BadRequestException;
import br.com.phoebus.desafiostarwars.model.Item;
import br.com.phoebus.desafiostarwars.model.Localizacao;
import br.com.phoebus.desafiostarwars.model.Rebelde;
import br.com.phoebus.desafiostarwars.model.Relatorio;
import br.com.phoebus.desafiostarwars.repository.RebeldeRepository;
import br.com.phoebus.desafiostarwars.service.RebeldeService;
import br.com.phoebus.desafiostarwars.util.MensagensDeErro;

@Service
public class RebeldeServiceImpl implements RebeldeService {

	@Autowired
	private RebeldeRepository repository;
	
	@Override
	public Rebelde criar(Rebelde rebelde) {

		if (rebelde.getInventario() == null)
			throw new BadRequestException("Rebelde deve ter um inventário!");
		
		List<Item> itens = rebelde.getInventario().getItens();
		
		if (itens == null || itens.size() == 0)
			throw new BadRequestException("Rebelde deve ter no mínimo um item no seu inventário!");
		
		rebelde.setReportCont(0);
		
		rebelde.getInventario().setTotalPontos(obterPontos(rebelde.getInventario().getItens()));
		
		return repository.save(rebelde);
	}

	@Override
	public Rebelde atualizar(Long rebeldeId, Rebelde rebelde) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizarLocalizacao(Long rebeldeId, Localizacao localizacao) {
		Rebelde rebeldeDB = recuperarPorId(rebeldeId);
		if (rebeldeDB != null)
			rebeldeDB.setLocalizacao(localizacao);
		
		repository.save(rebeldeDB);
	}

	@Override
	public List<Rebelde> listar() {
		return repository.findAll();
	}

	@Override
	public Rebelde recuperarPorId(Long rebeldeId) {
		Optional<Rebelde> rebelde = repository.findById(rebeldeId);
		
		if (rebelde == null || !rebelde.isPresent())
			throw new ResourceNotFoundException("Rebelde não encontrado!");
		
		return rebelde.get();
	}

	@Override
	public void reportar(Long rebeldeId) {
		Rebelde rebeldeDB = recuperarPorId(rebeldeId);
		
		rebeldeDB.setReportCont(rebeldeDB.getReportCont() + 1);
		
		repository.save(rebeldeDB);
	}

	@Override
	public void negociar(Long rebelde1Id, Long rebelde2Id, List<Item> itensRebelde1, 
			List<Item> itensRebelde2) {
		
		Rebelde rebelde1 = recuperarPorId(rebelde1Id);
		verificarRebeldeTraidor(rebelde1);
		
		Rebelde rebelde2 = recuperarPorId(rebelde2Id);
		verificarRebeldeTraidor(rebelde2);
		
		if (obterPontos(itensRebelde1) != obterPontos(itensRebelde2))
			throw new BadRequestException(MensagensDeErro.MSG_QTD_PONTOS_DIFERENTES);
		
		verificarItens(rebelde1, itensRebelde1);
		verificarItens(rebelde2, itensRebelde2);
		
		substituirItens(rebelde1, itensRebelde1, itensRebelde2);
		substituirItens(rebelde2, itensRebelde2, itensRebelde1);
		
		repository.save(rebelde1);
		repository.save(rebelde2);
		
	}

	@Override
	public Relatorio obterRelatorio() {
		
		List<Rebelde> rebeldes = repository.findAll();
		int contTraidor = 0;
		int contRebelde = 0;
		int contPontosPerdidos = 0;
		double contArma = 0;
		double contMunicao = 0;
		double contAgua = 0;
		double contComida = 0;
		
		for (Rebelde rebelde : rebeldes) {
			
			if (rebelde.getReportCont() >= 3) {
				contTraidor++;
				contPontosPerdidos += rebelde.getInventario().getTotalPontos();
			} else {
				contRebelde++;
				for (Item item: rebelde.getInventario().getItens()) {
					switch (item.getNome()) {
						case ARMA:
							contArma += item.getQuantidade(); 
							break;
						case MUNICAO:
							contMunicao += item.getQuantidade();
							break;
						case AGUA:
							contAgua += item.getQuantidade();
							break;
						case COMIDA:
							contComida += item.getQuantidade();
							break;
					}
				}
			}	
		}
		
		Relatorio relatorio = new Relatorio();
		relatorio.setPorcentagemRebeldes(new BigDecimal(contRebelde * 100.0 / rebeldes.size()).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setPorcentagemTraidores(new BigDecimal(contTraidor * 100.0 / rebeldes.size()).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setQtdMediaArma(new BigDecimal(contArma / contRebelde).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setQtdMediaMunicao(new BigDecimal(contMunicao / contRebelde).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setQtdMediaAgua(new BigDecimal(contAgua / contRebelde).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setQtdMediaComida(new BigDecimal(contComida / contRebelde).setScale(2, 
				BigDecimal.ROUND_FLOOR).doubleValue());
		relatorio.setPontosPerdidos(contPontosPerdidos);
		
		return relatorio;
	}
	
	private void verificarRebeldeTraidor(Rebelde rebelde) {
		if (rebelde.getReportCont() >= 3)
			throw new BadRequestException(MensagensDeErro.MSG_REBELDE_TRAIDOR.replace("{rebelde}", 
					rebelde.getNome()));
	}
	
	
	private void verificarItens(Rebelde rebelde, List<Item> itens) {
		if (obterPontos(itens) > obterPontos(rebelde.getInventario().getItens()))
			throw new BadRequestException(MensagensDeErro.MSG_ITENS_REBELDE_EXCEDE
					.replace("{rebelde}", rebelde.getNome()));
	}
	
	private int obterPontos(List<Item> itens) {
		int pontos = 0;
		for (Item item : itens) {
			
			switch (item.getNome()) {
				case ARMA:
					pontos += 4 * item.getQuantidade();
					break;
				case MUNICAO:
					pontos += 3 * item.getQuantidade();
					break;
				case AGUA:
					pontos += 2 * item.getQuantidade();
					break;
				case COMIDA:
					pontos += 1 * item.getQuantidade();
					break;
			}
		}
		
		return pontos;
	}
	
	private void substituirItens(Rebelde rebelde, List<Item> itensParaRemover, 
			List<Item> itensParaAdicionar) {
		
		List<Item> itensAtuais = rebelde.getInventario().getItens();
		
		Iterator<Item> itensAtuaisIterator = itensAtuais.iterator();
		
		while(itensAtuaisIterator.hasNext()) {
			Item itemAtual = itensAtuaisIterator.next();
			for (Item itemParaRemover : itensParaRemover) {
				if (itemAtual.getNome().equals(itemParaRemover.getNome())) {
					if (itemParaRemover.getQuantidade() > itemAtual.getQuantidade())
						throw new BadRequestException(MensagensDeErro
								.MSG_QTD_ITEM_TROCA_MAIOR_QUE_INVETARIO.replace("{item}", 
										itemParaRemover.getNome().getTipo()));
					int qtd = itemAtual.getQuantidade() - itemParaRemover.getQuantidade();
					if (qtd == 0) {
						itensAtuaisIterator.remove();
						continue;
					}
					itemAtual.setQuantidade(qtd);
				}
			}
		}
		// Adicionar Itens
		for (Item itemParaAdicionar : itensParaAdicionar) {
			Item item = itensAtuais.stream().filter(itemAtual -> itemAtual.getNome().equals(itemParaAdicionar.getNome())).findAny().orElse(null);
			if (item != null)
				item.setQuantidade(item.getQuantidade() + itemParaAdicionar.getQuantidade());
			else
				itensAtuais.add(itemParaAdicionar);
		}
		
	}

}
