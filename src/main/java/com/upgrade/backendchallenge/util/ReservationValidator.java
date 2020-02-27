package com.upgrade.backendchallenge.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.exception.ReservationException;

public class ReservationValidator {

	public static void validateReservation(ReservationDTO dto) {

		if (ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) > 3)
			throw new ReservationException("Can't book reservation for more than three days.");

		LocalDate today = LocalDate.now();

		if (today.compareTo(dto.getStartDate()) >= 0)
			throw new ReservationException("It's too late to make this reservation.");

		LocalDate inOneMonth = today.plusMonths(1);

		if (inOneMonth.compareTo(dto.getEndDate()) < 0)
			throw new ReservationException("It's too early to make this reservation.");
	}

}
