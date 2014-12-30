package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

public class MatriculaCollection {

	private String pattern;
	private List<Link> links;
	private List<Matricula> matriculas;
	
	
	public MatriculaCollection(){
		super();
		matriculas = new ArrayList<>();
	}
	public void addMatricula(Matricula matricula){
		matriculas.add(matricula);
	}
	
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Matricula> getMatriculas() {
		return matriculas;
	}
	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	
	
}
