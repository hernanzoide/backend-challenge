package com.upgrade.backendchallenge.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.upgrade.backendchallenge.model.Reservation;

@Component
public class ReservationRepository {
	
	private static final String DATABASE_NAME = "reservations";
	
	private Database reservationDatabase;
	
	@Autowired
	public ReservationRepository(CloudantClient cloudantClient) {
		this.reservationDatabase = cloudantClient.database(ReservationRepository.DATABASE_NAME, true);
	}
	
	public String create(Reservation reservation) {
		return this.reservationDatabase.save(reservation).getId();
	}
	
	public void edit(Reservation reservation) {
		this.reservationDatabase.update(reservation);
	}
	
	public void delete(Reservation reservation) {
		this.reservationDatabase.remove(reservation);
	}

	public Reservation get(String reservationId) {
		return this.reservationDatabase.find(Reservation.class,reservationId);
	}
}
