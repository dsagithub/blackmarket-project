package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
 

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.BlackError;
 
@Provider
public class WebApplicationExceptionMapper implements
		ExceptionMapper<WebApplicationException> {
	@Override
	public Response toResponse(WebApplicationException exception) {
		BlackError error = new BlackError(
				exception.getResponse().getStatus(), exception.getMessage());
		return Response.status(error.getStatus()).entity(error)
				.type(MediaType.BLACKS_API_ERROR).build();
	}
 
}