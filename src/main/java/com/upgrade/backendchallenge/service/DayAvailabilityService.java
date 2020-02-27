package com.upgrade.backendchallenge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.upgrade.backendchallenge.exception.ReservationException;
import com.upgrade.backendchallenge.model.DayAvailability;
import com.upgrade.backendchallenge.repository.DayAvailabilityRepository;

@Component
public class DayAvailabilityService {

	@Value("${island.max_occupancy}")
	private int max_occupancy;

	@Autowired
	private DayAvailabilityRepository dayAvailabilityRepository;

	public List<DayAvailability> get(LocalDate startDate, LocalDate endDate) {
		return this.dayAvailabilityRepository.get(startDate.toString(),endDate.toString());
	}

	public synchronized void updateAvailability(List<DayAvailability> newAvailabilities) {
		List<DayAvailability> updatedAvailabilities = new ArrayList<DayAvailability>();
		DayAvailability updatedAvailability;
		for (DayAvailability availability : newAvailabilities) {
			updatedAvailability = (this.dayAvailabilityRepository.contains(availability.getId())
					? this.dayAvailabilityRepository.get(availability.getId())
					: new DayAvailability(availability.getDay()));
			if (availability.getOccupancy() + updatedAvailability.getOccupancy() > this.max_occupancy)
				throw new ReservationException("Occupancy rate exceeded");
			updatedAvailability.setOccupancy(availability.getOccupancy() + updatedAvailability.getOccupancy());
			updatedAvailabilities.add(updatedAvailability);
		}
		this.dayAvailabilityRepository.createOrUpdateAll(updatedAvailabilities);
	}
}
