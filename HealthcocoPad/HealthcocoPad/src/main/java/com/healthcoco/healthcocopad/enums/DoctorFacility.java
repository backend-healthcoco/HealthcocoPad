package com.healthcoco.healthcocopad.enums;

public enum DoctorFacility {

	IBS("IBS"),CALL("CALL"),BOOK("BOOK");
	
	private String type;

	private DoctorFacility(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
