package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

public class Comentario {
	
	
	private List<Link> links;
	private int id_comentario;
	private String autor;
	private String id_contenido;
	private String comentario;
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getId_contenido() {
		return id_contenido;
	}
	public void setId_contenido(String id_contenido) {
		this.id_contenido = id_contenido;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getId_comentario() {
		return id_comentario;
	}
	public void setId_comentario(int id_comentario) {
		this.id_comentario = id_comentario;
	}
	
	

}
