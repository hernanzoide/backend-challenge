package com.upgrade.backendchallenge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.exception.ReservationException;
import com.upgrade.backendchallenge.model.DayAvailability;
import com.upgrade.backendchallenge.model.Reservation;
import com.upgrade.backendchallenge.repository.ReservationRepository;
import com.upgrade.backendchallenge.util.ReservationValidator;

@Component
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private DayAvailabilityService dayAvailabilityService;

	public long create(ReservationDTO dto) {
		ReservationValidator.validateReservation(dto);

		Reservation reservation = new Reservation(dto);

		List<DayAvailability> availabilities = createAvailabilties(reservation);

		this.dayAvailabilityService.updateAvailability(availabilities);

		return this.reservationRepository.save(reservation).getId();
	}

	private List<DayAvailability> createAvailabilties(Reservation reservation) {
		List<DayAvailability> availabilities = new ArrayList<DayAvailability>();
		DayAvailability availability;
		LocalDate availabilityDate = LocalDate.parse(reservation.getStartDate());
		while (availabilityDate.compareTo(LocalDate.parse(reservation.getEndDate())) != 0) {
			availability = new DayAvailability(availabilityDate.toString());
			availability.setOccupancy(reservation.getNumberOfPeople());
			availabilities.add(availability);
			availabilityDate = availabilityDate.plusDays(1);
		}
		return availabilities;
	}

	public void delete(long reservationId) {
		Reservation reservation = this.get(reservationId);
		reservation.setNumberOfPeople(-reservation.getNumberOfPeople());

		List<DayAvailability> availabilities = createAvailabilties(reservation);

		this.dayAvailabilityService.updateAvailability(availabilities);

		this.reservationRepository.delete(reservation);
	}

	public Reservation get(long reservationId) {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationException("Reservation not found. id:" + reservationId));
	}

	public void edit(long reservationId, ReservationDTO dto) {
		ReservationValidator.validateReservation(dto);
		Reservation reservation = new Reservation(dto);

		Reservation oldReservation = this.get(reservationId);
		oldReservation.setNumberOfPeople(-oldReservation.getNumberOfPeople());

		List<DayAvailability> newAvailabilities = createAvailabilties(reservation);
		List<DayAvailability> oldAvailabilities = createAvailabilties(oldReservation);

		this.dayAvailabilityService.updateAvailability(this.dayAvailabilityService.mergeAvailabilities(oldAvailabilities, newAvailabilities));

		oldReservation.setEmail(reservation.getEmail());
		oldReservation.setFullName(reservation.getFullName());
		oldReservation.setNumberOfPeople(reservation.getNumberOfPeople());
		oldReservation.setStartDate(reservation.getStartDate());
		oldReservation.setEndDate(reservation.getEndDate());

		this.reservationRepository.save(oldReservation);
	}

}
