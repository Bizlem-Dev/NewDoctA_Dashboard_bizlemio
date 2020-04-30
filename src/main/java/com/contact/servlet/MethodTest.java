package com.contact.servlet;

import javax.jcr.Node;

import com.service.impl.FreeTrialandCart;

public class MethodTest {

	// instance variable
	
	String instanceEmail;
	String instanceGroup;
	 
	 // create constructor 
	
	MethodTest(String email, String group){
		
		instanceEmail=email;
		instanceGroup=group;
		
	}
	
	String freeTrialCheck(){
			
			FreeTrialandCart cart=classReturn();
			String FreetrialStatus=cart.checkfreetrial(instanceEmail, "");
		
		return FreetrialStatus;
	}
	
	FreeTrialandCart classReturn(){
		
		FreeTrialandCart cart=new FreeTrialandCart();
		return cart;
		
	}
	
	void mainLogic(Node userEmailNode){
		
		
		
	}
	
	void printOutput(){
		
		System.out.println("mainPrint: "+freeTrialCheck());
	}
	
	public static void main(String [] args) {
		
		MethodTest mTest=new MethodTest("nilesh@gmail.com", "G1");
		mTest.printOutput();
	}
	
}
