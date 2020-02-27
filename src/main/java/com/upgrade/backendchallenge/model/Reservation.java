package com.upgrade.backendchallenge.model;

import java.time.LocalDate;

import com.cloudant.client.api.model.Document;
import com.upgrade.backendchallenge.dto.ReservationDTO;

public class Reservation extends Document {

	private String fullName;
	
	private String email;
	
	private int numberOfPeople;
	
	private LocalDate startDate;
	
	private LocalDate endDate;

	public Reservation() {
		
	}
	
	public Reservation(ReservationDTO dto) {
		this.email = dto.getEmail();
		this.fullName = dto.getFullName();
		this.numberOfPeople = dto.getNumberOfPeople();
		this.startDate = dto.getStartDate();
		this.endDate = dto.getEndDate();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
