package com.upgrade.backendchallenge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.upgrade.backendchallenge.dto.ReservationDTO;
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

	public String create(ReservationDTO dto) {
		ReservationValidator.validateReservation(dto);
		
		Reservation reservation = new Reservation(dto);
		
		List<DayAvailability> availabilities = createAvailabilties(reservation);
		
		this.dayAvailabilityService.updateAvailability(availabilities);
		
		return this.reservationRepository.create(reservation);
	}


	private List<DayAvailability> createAvailabilties(Reservation reservation) {
		List<DayAvailability> availabilities = new ArrayList<DayAvailability>();
		DayAvailability availability;
		LocalDate availabilityDate = reservation.getStartDate();
		while (availabilityDate.compareTo(reservation.getEndDate())!=0) {
			availability = new DayAvailability(availabilityDate);
			availability.setOccupancy(reservation.getNumberOfPeople());
			availabilities.add(availability);
			availabilityDate = availabilityDate.plusDays(1);
		}
		return availabilities;
	}
	
	public void delete(String reservationId) {
		Reservation reservation = this.reservationRepository.get(reservationId);
		reservation.setNumberOfPeople(-reservation.getNumberOfPeople());
		
		List<DayAvailability> availabilities = createAvailabilties(reservation);
		
		this.dayAvailabilityService.updateAvailability(availabilities);
		
		this.reservationRepository.delete(reservation);
	}

	public Reservation get(String reservationId) {
		return reservationRepository.get(reservationId);
	}
	
	
	public void edit(String reservationId, ReservationDTO dto) {
		ReservationValidator.validateReservation(dto);
		Reservation reservation = new Reservation(dto);
		
		Reservation oldReservation = this.reservationRepository.get(reservationId);
		oldReservation.setNumberOfPeople(-oldReservation.getNumberOfPeople());
		
		List<DayAvailability> newAvailabilities = createAvailabilties(reservation);
		List<DayAvailability> oldAvailabilities = createAvailabilties(oldReservation);
	
		this.dayAvailabilityService.updateAvailability(this.mergeAvailabilities(oldAvailabilities,newAvailabilities));
		
		oldReservation.setEmail(reservation.getEmail());
		oldReservation.setFullName(reservation.getFullName());
		oldReservation.setNumberOfPeople(reservation.getNumberOfPeople());
		oldReservation.setStartDate(reservation.getStartDate());
		oldReservation.setEndDate(reservation.getEndDate());
		
		this.reservationRepository.edit(oldReservation);
	}


	private List<DayAvailability> mergeAvailabilities(List<DayAvailability> oldAvailabilities,
			List<DayAvailability> newAvailabilities) {
		List<DayAvailability> mergedAvailabilities = new ArrayList<DayAvailability>();
		int newIndex = 0;
		int oldIndex = 0;
		
		while (newIndex<newAvailabilities.size() && oldIndex<oldAvailabilities.size()) {
			if (newAvailabilities.get(newIndex).getDay().compareTo(oldAvailabilities.get(oldIndex).getDay())==0) {
				DayAvailability merged = new DayAvailability(newAvailabilities.get(newIndex).getDay());
				merged.setOccupancy(newAvailabilities.get(newIndex).getOccupancy()+oldAvailabilities.get(oldIndex).getOccupancy());
				mergedAvailabilities.add(merged);
				newIndex++;
				oldIndex++;
			}
			else {
				if (newAvailabilities.get(0).getDay().compareTo(oldAvailabilities.get(0).getDay())<0) {
					mergedAvailabilities.add((newAvailabilities.get(newIndex)));
					newIndex++;
				}
				else {
					mergedAvailabilities.add(oldAvailabilities.get(oldIndex));
					oldIndex++;
				}
			}
		}	
		while (newIndex<newAvailabilities.size()) {
			mergedAvailabilities.add(newAvailabilities.get(newIndex));
			newIndex++;
		}
		while (oldIndex<oldAvailabilities.size()) {
			mergedAvailabilities.add(oldAvailabilities.get(oldIndex));
			oldIndex++;
		}
		
		return mergedAvailabilities;
	}



}
