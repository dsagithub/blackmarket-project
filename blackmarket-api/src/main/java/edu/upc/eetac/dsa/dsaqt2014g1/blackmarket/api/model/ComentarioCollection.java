package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

public class ComentarioCollection {

	
	
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	private String pattern;
	private List<Comentario> comentarios;
	
	public ComentarioCollection(){
		super();
		comentarios = new ArrayList<>();
	}
	public void addComentario(Comentario comentario){
		comentarios.add(comentario);
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}


	
	
	
	
	
	
}
