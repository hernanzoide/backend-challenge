package com.upgrade.backendchallenge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.upgrade.backendchallenge.dto.ReservationDTO;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

	private static final long serialVersionUID = 5613572157908329904L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column
	private String fullName;

	@Column
	private String email;

	@Column
	private int numberOfPeople;

	@Column
	private String startDate;

	@Column
	private String endDate;

	public Reservation() {

	}

	public Reservation(ReservationDTO dto) {
		this.email = dto.getEmail();
		this.fullName = dto.getFullName();
		this.numberOfPeople = dto.getNumberOfPeople();
		this.startDate = dto.getStartDate().toString();
		this.endDate = dto.getEndDate().toString();
	}

	public long getId() {
		return id;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
