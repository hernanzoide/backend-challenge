package com.upgrade.backendchallenge.dto;

import com.upgrade.backendchallenge.model.DayAvailability;

public class DayAvailabilityDTO {

	private String _id;
	
	private String _rev;
	
	private int occupancy;
	
	public DayAvailabilityDTO(DayAvailability dayAvailability) {
		this._id = dayAvailability.getId();
		this._rev = dayAvailability.getRevision();
		this.occupancy = dayAvailability.getOccupancy();
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getRev() {
		return _rev;
	}

	public void setRev(String _rev) {
		this._rev = _rev;
	}

	public int getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}
	
}
