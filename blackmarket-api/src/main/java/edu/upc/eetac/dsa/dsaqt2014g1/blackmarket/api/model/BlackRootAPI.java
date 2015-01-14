package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.BlackResource;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.BlackRootAPIResource;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.MediaType2;


public class BlackRootAPI {

	@InjectLinks({
		@InjectLink(resource = BlackRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "BlackMarket Root API", method = "getRootAPI"),
		@InjectLink(resource = BlackResource.class, style = Style.ABSOLUTE, rel = "stings", title = "Latest stings", type = MediaType2.BLACKS_API_BLACK),
		@InjectLink(resource = BlackResource.class, style = Style.ABSOLUTE, rel = "create-stings", title = "Latest stings", type = MediaType2.BLACKS_API_BLACK) })
	
	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
	
}
