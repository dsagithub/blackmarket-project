package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BlackApplication  extends ResourceConfig {

	public BlackApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
	}
}
