package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;



public class Black {

	//@InjectLinks({
		//@InjectLink(resource = StingResource.class, style = Style.ABSOLUTE, rel = "stings", title = "Latest stings", type = MediaType.BEETER_API_STING_COLLECTION),
		//@InjectLink(resource = StingResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Sting", type = MediaType.BEETER_API_STING, method = "getSting", bindings = @Binding(name = "stingid", value = "${instance.stingid}")) })
private List<Link> links;
private String id_contenido;
private int id_asignatura;
private int id_tipo;
private String titulo;
private String descripcion;
private String autor;
private String link;
private int invalid;
private long fecha;
private long creationTimestamp;


public long getCreationTimestamp() {
	return creationTimestamp;
}
public void setCreationTimestamp(long creationTimestamp) {
	this.creationTimestamp = creationTimestamp;
}
public List<Link> getLinks() {
	return links;
}
public void setLinks(List<Link> links) {
	this.links = links;
}
public String getId_contenido() {
	return id_contenido;
}
public void setId_contenido(String id_contenido) {
	this.id_contenido = id_contenido;
}
public int getId_asignatura() {
	return id_asignatura;
}
public void setId_asignatura(int id_asignatura) {
	this.id_asignatura = id_asignatura;
}
public int getId_tipo() {
	return id_tipo;
}
public void setId_tipo(int id_tipo) {
	this.id_tipo = id_tipo;
}
public String getTitulo() {
	return titulo;
}
public void setTitulo(String titulo) {
	this.titulo = titulo;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public String getAutor() {
	return autor;
}
public void setAutor(String autor) {
	this.autor = autor;
}
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public int getInvalid() {
	return invalid;
}
public void setInvalid(int invalid) {
	this.invalid = invalid;
}
public long getFecha() {
	return fecha;
}
public void setFecha(long fecha) {
	this.fecha = fecha;
}


	
	
	
}
