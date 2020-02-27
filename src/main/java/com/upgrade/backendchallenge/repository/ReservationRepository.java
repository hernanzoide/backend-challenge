package com.upgrade.backendchallenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.upgrade.backendchallenge.model.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>{
	
	
}
