package com.ideas.domain;

public class Address {
	private final double latitude;
	private final double longitude;
	private String address;
	
	public Address(double latitude, double longitude, String address) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
}
