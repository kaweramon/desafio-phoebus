package br.com.phoebus.desafiostarwars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.phoebus.desafiostarwars.model.Genero;
import br.com.phoebus.desafiostarwars.model.Inventario;
import br.com.phoebus.desafiostarwars.model.Item;
import br.com.phoebus.desafiostarwars.model.ItemTipo;
import br.com.phoebus.desafiostarwars.model.Localizacao;
import br.com.phoebus.desafiostarwars.model.Rebelde;
import br.com.phoebus.desafiostarwars.service.RebeldeService;
import br.com.phoebus.desafiostarwars.util.MensagensDeErro;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DesafioStarWarsApplicationTests {

	@Autowired
	private RebeldeService service;
	
	@Test
	public void testeSalvarRebelde() {
		
		Rebelde rebelde1 = new Rebelde();
		rebelde1.setNome("Luke Skywalker");
		rebelde1.setGenero(Genero.M);
		rebelde1.setIdade(45);
		
		Inventario inventario1 = new Inventario();
		List<Item> itens = new ArrayList<Item>();
		
		itens.add(new Item(ItemTipo.ARMA, 2));
		itens.add(new Item(ItemTipo.MUNICAO, 50));
		itens.add(new Item(ItemTipo.AGUA, 10));
		itens.add(new Item(ItemTipo.COMIDA, 20));
		
		inventario1.setItens(itens);
		rebelde1.setInventario(inventario1);
		
		Rebelde rebeldeSalvo = service.criar(rebelde1);
		
		assertEquals(rebeldeSalvo.getNome(), "Luke Skywalker");
		assertTrue(rebeldeSalvo.getGenero().equals(Genero.M));
		assertTrue(rebeldeSalvo.getInventario().getTotalPontos() == 198);
		
		Rebelde rebelde2 = new Rebelde();
		rebelde2.setNome("Anakin Skywalker");
		rebelde2.setGenero(Genero.M);
		rebelde2.setIdade(20);
		
		Inventario inventario2 = new Inventario();
		List<Item> itens2 = new ArrayList<Item>();
		
		itens2.add(new Item(ItemTipo.ARMA, 1));
		itens2.add(new Item(ItemTipo.MUNICAO, 30));
		itens2.add(new Item(ItemTipo.AGUA, 15));
		itens2.add(new Item(ItemTipo.COMIDA, 17));
		
		inventario2.setItens(itens2);
		rebelde2.setInventario(inventario2);
		
		Rebelde rebeldeSalvo2 = service.criar(rebelde2);
		
		// Verifica o inventário
		assertNotNull(rebeldeSalvo2.getInventario());
		// Verifica a idade
		assertTrue(rebelde2.getIdade() == 20);
		// Verifica a quantidade de Munição
		assertEquals(30, rebelde2.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.MUNICAO)).findFirst().get().getQuantidade());
		
		Rebelde rebelde3 = new Rebelde();
		rebelde3.setNome("Obi-Wan Kenobi");
		rebelde3.setGenero(Genero.M);
		rebelde3.setIdade(60);
		
		Inventario inventario3 = new Inventario();
		List<Item> itens3 = new ArrayList<Item>();
		
		itens3.add(new Item(ItemTipo.ARMA, 2));
		itens3.add(new Item(ItemTipo.MUNICAO, 10));
		itens3.add(new Item(ItemTipo.AGUA, 5));
		itens3.add(new Item(ItemTipo.COMIDA, 7));
		
		inventario3.setItens(itens3);
		rebelde3.setInventario(inventario3);
		
		Rebelde rebeldeSalvo3 = service.criar(rebelde3);
		
		// Verifica a quantidade de comida
		assertEquals(7,  rebeldeSalvo3.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.COMIDA)).findFirst().get().getQuantidade());
		
	}

	@Test
	public void atualizarLocalizacoes() {
		Localizacao localizacao = new Localizacao();
		localizacao.setLatitude((float) -7.1122196);
		localizacao.setLongitude((float) -34.8282775);
		localizacao.setNomeGalaxia("Tambaú");
		
		service.atualizarLocalizacao((long) 1, localizacao);
		
		Rebelde rebelde1 = service.recuperarPorId((long) 1);
		
		assertTrue(localizacao.getLatitude() == rebelde1.getLocalizacao().getLatitude());
		assertTrue(localizacao.getLongitude() == rebelde1.getLocalizacao().getLongitude());
		assertEquals("Tambaú", rebelde1.getLocalizacao().getNomeGalaxia());
		
		Localizacao localizacao2 = new Localizacao();
		localizacao2.setLatitude((float) -7.1006083);
		localizacao2.setLongitude((float) -34.837581);
		localizacao2.setNomeGalaxia("Manaíra");
		
		service.atualizarLocalizacao((long) 1, localizacao2);
		
		Rebelde rebelde2 = service.recuperarPorId((long) 1);
		
		assertTrue(localizacao2.getLatitude() == rebelde2.getLocalizacao().getLatitude());
		assertTrue(localizacao2.getLongitude() == rebelde2.getLocalizacao().getLongitude());
		assertEquals("Manaíra", rebelde2.getLocalizacao().getNomeGalaxia());
		
		
		Localizacao localizacao3 = new Localizacao();
		localizacao3.setLatitude((float) -7.0683243);
		localizacao3.setLongitude((float) -34.8431006);
		localizacao3.setNomeGalaxia("Bessa");
		
		service.atualizarLocalizacao((long) 1, localizacao3);
		
		Rebelde rebelde3 = service.recuperarPorId((long) 1);
		
		assertTrue(localizacao3.getLatitude() == rebelde3.getLocalizacao().getLatitude());
		assertTrue(localizacao3.getLongitude() == rebelde3.getLocalizacao().getLongitude());
		assertEquals("Bessa", rebelde3.getLocalizacao().getNomeGalaxia());
	}
	
	@Test
	public void negociar() {
		
		List<Item> itens1 = new ArrayList<Item>();
		
		itens1.add(new Item(ItemTipo.ARMA, 1));
		
		List<Item> itens2 = new ArrayList<Item>();
		itens2.add(new Item(ItemTipo.AGUA, 2));
		
		service.negociar((long) 1, (long) 2, itens1, itens2);
		
		Rebelde rebelde1 = service.recuperarPorId((long) 1);
		Rebelde rebelde2 = service.recuperarPorId((long) 2);
		
		assertEquals(1, rebelde1.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.ARMA)).findFirst().get().getQuantidade());
		
		assertEquals(12, rebelde1.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.AGUA)).findFirst().get().getQuantidade());
		
		assertEquals(2, rebelde2.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.ARMA)).findFirst().get().getQuantidade());
		
		assertEquals(13, rebelde2.getInventario().getItens().stream().filter(
				item -> item.getNome().equals(ItemTipo.AGUA)).findFirst().get().getQuantidade());
	}
	
	@Test
	public void negociarItensComPontosDiferentes() {
		
		List<Item> itens1 = new ArrayList<Item>();
		itens1.add(new Item(ItemTipo.ARMA, 3));
		
		List<Item> itens2 = new ArrayList<Item>();
		itens2.add(new Item(ItemTipo.COMIDA, 20));
		try {
			service.negociar((long) 1, (long) 2, itens1, itens2);
		} catch (Exception e) {
			assertEquals(MensagensDeErro.MSG_QTD_PONTOS_DIFERENTES, e.getMessage());
		}
	}
	
}
