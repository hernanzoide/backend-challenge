package com.upgrade.backendchallenge.model;

import java.time.LocalDate;

import com.cloudant.client.api.model.Document;
import com.upgrade.backendchallenge.dto.DayAvailabilityDTO;

public class DayAvailability extends Document {
	
    public static final String ID_PATTERN = "yyyy-MM-dd";

	private LocalDate day;
	
	private int occupancy;
	
	public DayAvailability() {
	}
	
	public DayAvailability(LocalDate date) {
		this.setId(date.toString());
		this.day = date;
		this.occupancy = 0;
	}
	
	public DayAvailability(DayAvailabilityDTO dto) {
		this.setId(dto.getId());
		this.setRevision(dto.getRev());
		this.occupancy = dto.getOccupancy();
	}

	public int getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}
	
	public LocalDate getDay() {
		return day;
	}
	
}
