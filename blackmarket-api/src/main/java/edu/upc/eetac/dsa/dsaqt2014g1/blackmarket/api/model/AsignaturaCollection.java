package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.AsignaturaResource;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.BlackResource;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.MediaType2;

public class AsignaturaCollection {
	

	
	
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
		

	
	private String pattern;
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

	public List<Asignatura> getAsignaturas() {
		return asignaturas;
	}
	public void setAsignaturas(List<Asignatura> asignaturas) {
		this.asignaturas = asignaturas;
	}
	
	

	
}
