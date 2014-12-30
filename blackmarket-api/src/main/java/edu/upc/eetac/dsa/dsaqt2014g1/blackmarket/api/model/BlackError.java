package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model;

public class BlackError {

	private int status;
	private String message;
 
	public BlackError() {
		super();
	}
 
	public BlackError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
 
	public int getStatus() {
		return status;
	}
 
	public void setStatus(int status) {
		this.status = status;
	}
 
	public String getMessage() {
		return message;
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
