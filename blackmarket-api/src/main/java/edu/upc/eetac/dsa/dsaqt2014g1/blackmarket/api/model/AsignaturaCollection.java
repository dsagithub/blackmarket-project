package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

public class AsignaturaCollection {
	
	private String pattern;
	private List<Link> links;
	private List<Asignatura> asignaturas;
	
	public AsignaturaCollection(){
		super();
		asignaturas = new ArrayList<>();
	}
	public void addAsignatura(Asignatura asignatura){
		asignaturas.add(asignatura);
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
	public List<Asignatura> getAsignaturas() {
		return asignaturas;
	}
	public void setAsignaturas(List<Asignatura> asignaturas) {
		this.asignaturas = asignaturas;
	}
	

	
}
