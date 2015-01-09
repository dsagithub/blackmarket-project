package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

public class Matricula {
	
	
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	private String username_matriculas;
	private int id_asignatura_u_matriculas;
	
	public String getUsername_matriculas() {
		return username_matriculas;
	}
	public void setUsername_matriculas(String username_matriculas) {
		this.username_matriculas = username_matriculas;
	}
	public int getId_asignatura_u_matriculas() {
		return id_asignatura_u_matriculas;
	}
	public void setId_asignatura_u_matriculas(int id_asignatura_u_matriculas) {
		this.id_asignatura_u_matriculas = id_asignatura_u_matriculas;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
