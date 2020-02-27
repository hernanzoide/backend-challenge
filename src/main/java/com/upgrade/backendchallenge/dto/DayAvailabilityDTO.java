package com.upgrade.backendchallenge.dto;

import java.time.LocalDate;

import com.upgrade.backendchallenge.model.DayAvailability;

public class DayAvailabilityDTO {

	private long id;

	private LocalDate day;

	private int occupancy;

	public DayAvailabilityDTO(DayAvailability dayAvailability) {
		this.id = dayAvailability.getId();
		this.day = LocalDate.parse(dayAvailability.getDay());
		this.occupancy = dayAvailability.getOccupancy();
	}

	public long getId() {
		return id;
	}

	public void setId(long _id) {
		this.id = _id;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public int getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}

}
