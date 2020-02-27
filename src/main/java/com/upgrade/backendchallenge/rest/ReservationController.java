package com.upgrade.backendchallenge.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.exception.ReservationException;
import com.upgrade.backendchallenge.model.Reservation;
import com.upgrade.backendchallenge.service.ReservationService;

@RestController
@RequestMapping("/reservation/")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping("/{reservationId}")
	public ResponseEntity<Reservation> get(@PathVariable("reservationId") long reservationId) {

		Reservation reservation = this.reservationService.get(reservationId);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

	@PutMapping("/{reservationId}")
	public ResponseEntity<Reservation> editReservation(@PathVariable("reservationId") long reservationId,
			@RequestBody ReservationDTO reservation) {

		this.reservationService.edit(reservationId, reservation);
		return new ResponseEntity<Reservation>(HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Long> createReservation(@RequestBody ReservationDTO reservation) {

		return new ResponseEntity<Long>(this.reservationService.create(reservation), HttpStatus.OK);
	}

	@DeleteMapping("/{reservationId}")
	public ResponseEntity<Reservation> delete(@PathVariable("reservationId") long reservationId) {

		this.reservationService.delete(reservationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<?> handleReservationException(ReservationException exc) {
		return ResponseEntity.badRequest().body(exc.getMessage());
	}

	@ExceptionHandler(NoDocumentException.class)
	public ResponseEntity<?> handleDocumentNotFound(NoDocumentException exc) {
		return ResponseEntity.badRequest().body("Reservation does not exist: " + exc.getMessage());
	}

}
