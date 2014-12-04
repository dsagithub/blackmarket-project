package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.BlackRootAPI;

@Path("/")
public class BlackRootAPIResource {

	@GET
	public BlackRootAPI getRootAPI() {
		BlackRootAPI api = new BlackRootAPI();
		return api;
	}
	
}
