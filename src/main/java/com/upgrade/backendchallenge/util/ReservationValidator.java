package com.upgrade.backendchallenge.util;

import java.util.Date;

import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.exception.ReservationException;

public class ReservationValidator {

	public static void validateReservation(ReservationDTO dto) {
		
		if (DateUtils.getDifferenceDays(dto.getStartDate(), dto.getEndDate())>3)
			throw new ReservationException("Can't book reservation for more than three days.");
		
		Date today = DateUtils.getTodayDate();
		Date firstDay = DateUtils.resetTime(dto.getStartDate());
		
		if (today.compareTo(firstDay)>=0)
			throw new ReservationException("It's too late to make this reservation.");
		
		Date inOneMonth = DateUtils.addMonth(today);
		
		Date lastDay = DateUtils.resetTime(dto.getEndDate());

		if (inOneMonth.compareTo(lastDay)<0)
			throw new ReservationException("It's too early to make this reservation.");
	}
	
}
