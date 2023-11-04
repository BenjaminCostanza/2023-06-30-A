package it.polito.tdp.exam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.exam.db.BaseballDAO;

public class Model {
	
	private BaseballDAO dao;
	private SimpleWeightedGraph<Integer, DefaultWeightedEdge> grafo;
	private Map<Integer,List<People>> annoToPlayers;
	
	public Model() {
		this.dao = new BaseballDAO();
		this.annoToPlayers = new HashMap<Integer, List<People>>();
	}
	
	public void creaGrafo(String name) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici
		List<Integer> vertici = this.dao.getVertici(name);
		Graphs.addAllVertices(this.grafo,vertici);
		
		
		for (int anno : vertici) {
			this.annoToPlayers.put(anno, this.dao.getPlayersTeamYear(name, anno) );
		}
		
		for (int i = 0; i <vertici.size(); i++) {
			for (int j = i+1; j < vertici.size(); j++) {
				List<People> giocatori1 = new ArrayList<People>(this.annoToPlayers.get(vertici.get(i)));
				List<People> giocatori2 = this.annoToPlayers.get(vertici.get(j));
				giocatori1.retainAll(giocatori2);
				int peso = giocatori1.size();
				Graphs.addEdgeWithVertices(this.grafo, vertici.get(i), vertici.get(j), peso);
			}
		}
		
	}
	
	
	public List<Adiacenza> getElencoAdiacenti(Integer anno){
		List<Adiacenza> result = new ArrayList<>();
		for(Integer i : Graphs.neighborListOf(this.grafo, anno)) {
			DefaultWeightedEdge dwe = this.grafo.getEdge(i, anno);
			result.add(new Adiacenza(this.grafo.getEdgeSource(dwe), this.grafo.getEdgeTarget(dwe), (int)this.grafo.getEdgeWeight(dwe)));
		}
		return result;
		
	}

	public List<String> getAllName() {
		List<String> result = dao.getTeamsNames();
		return result;
	}
	
	public Set<Integer> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
}
