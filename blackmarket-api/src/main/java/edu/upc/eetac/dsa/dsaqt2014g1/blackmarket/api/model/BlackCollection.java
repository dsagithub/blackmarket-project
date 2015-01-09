 package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;


import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.BlackResource;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.MediaType2;





public class BlackCollection {

	@InjectLinks({
		@InjectLink(resource = BlackResource.class, style = Style.ABSOLUTE, rel = "create-black", title = "Create black", type = MediaType2.BLACKS_API_BLACK),
		@InjectLink(value = "/search/autor/{nombreautor}?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous black", type = MediaType2.BLACKS_API_BLACK_COLLECTION, bindings = {
				@Binding(name = "before", value = "${instance.oldestTimestamp}"),
				@Binding(name = "nombreautor", value = "${instance.pattern}") }),
		@InjectLink(value = "/search/autor/{nombreautor}?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest black", type = MediaType2.BLACKS_API_BLACK_COLLECTION, bindings = {
				@Binding(name = "after", value = "${instance.newestTimestamp}"),
				@Binding(name = "nombreautor", value = "${instance.pattern}") }),
@InjectLink(value = "/search/titulo/{titulo}?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest black", type = MediaType2.BLACKS_API_BLACK_COLLECTION, bindings = {
		@Binding(name = "after", value = "${instance.newestTimestamp}"),
		@Binding(name = "titulo", value = "${instance.pattern}") }),
@InjectLink(value = "/search/titulo/{titulo}?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous black", type = MediaType2.BLACKS_API_BLACK_COLLECTION, bindings = {
				@Binding(name = "before", value = "${instance.oldestTimestamp}"),
				@Binding(name = "titulo", value = "${instance.pattern}") })})
	
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
	
private String pattern;
private List<Black> blacks;
private long newestTimestamp;
private long oldestTimestamp;

public BlackCollection(){
	super();
	blacks = new ArrayList<>();
}
public void addBlack(Black black){
	blacks.add(black);
}

public void setBlacks(List<Black> blacks) {
	this.blacks = blacks;
}


public List<Black> getBlacks() {
	return blacks;
}
public void setBlack(List<Black> black) {
	this.blacks = black;
}
public long getNewestTimestamp() {
	return newestTimestamp;
}
public void setNewestTimestamp(long newestTimestamp) {
	this.newestTimestamp = newestTimestamp;
}
public long getOldestTimestamp() {
	return oldestTimestamp;
}
public void setOldestTimestamp(long oldestTimestamp) {
	this.oldestTimestamp = oldestTimestamp;
}
public String getPattern() {
	return pattern;
}

public void setPattern(String pattern) {
	this.pattern = pattern;
}





	
}
