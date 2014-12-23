package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

public interface MediaType {

	public final static String BLACKS_API_BLACK = "application/vnd.blackmarket.api.black+json";
	public final static String BLACKS_API_BLACK_COLLECTION = "application/vnd.blackmarket.api.black.collection+json";
	
	public final static String BLACKS_API_USER = "application/vnd.blackmarket.api.user+json";
	public final static String BLACKS_API_USER_COLLECTION = "application/vnd.blackmarket.api.user.collection+json";
	
	public final static String BLACKS_API_ASIGNATURA = "application/vnd.blackmarket.api.asignatura+json";
	public final static String BLACKS_API_ASIGNATURA_COLLECTION = "application/vnd.blackmarket.api.asignatura.collection+json";
	
	public final static String BLACKS_API_MATRICULA= "application/vnd.blackmarket.api.matricula+json";
	public final static String BLACKS_API_MATRICULA_COLLECTION= "application/vnd.blackmarket.api.matricula.collection+json";
	
	
	public final static String BLACKS_API_ERROR = "application/vnd.dsa.blackmarket.error+json";
	//preguntar bien las rutas de porque unos tienen dsa y tal. 
}
