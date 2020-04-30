package com.service.servlet;

public class PojoClass {

	private String email;
	
	 private static final PojoClass INSTANCE = new PojoClass();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	 public static PojoClass getInstance() {
	        return INSTANCE;
	    }
	

}
