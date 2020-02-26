package com.upgrade.backendchallenge.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cloudant.client.api.model.Document;
import com.upgrade.backendchallenge.dto.DayAvailabilityDTO;

public class DayAvailability extends Document {
	
	public static final String ID_PATTERN = "yyyy-MM-dd";
	
	private Date day;
	
	private int occupancy;
	
	
	public DayAvailability(Date date) {
		this.setId(DayAvailability.getDayAvailavilityId(date));
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
	
	public static String getDayAvailavilityId(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DayAvailability.ID_PATTERN);
		return format.format(date);
	}

	public Date getDay() {
		return day;
	}
	
}
