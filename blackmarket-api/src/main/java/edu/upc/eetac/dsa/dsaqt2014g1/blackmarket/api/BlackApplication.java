package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BlackApplication  extends ResourceConfig {

	public BlackApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
		register(MultiPartFeature.class);
		ResourceBundle bundle = ResourceBundle.getBundle("application");

		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			property(key, bundle.getObject(key));
			
			
		}
	}
}
