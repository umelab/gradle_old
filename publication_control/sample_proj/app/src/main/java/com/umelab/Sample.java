package com.umelab;

public class Sample {
	
	public Sample(){
		SampleAA sampleAA = new SampleAA();
		System.out.println("Create aa component: " + new SampleAA().getAAname());
	}
	
	public static void main(String args[]) {
		new Sample();
	}
}
