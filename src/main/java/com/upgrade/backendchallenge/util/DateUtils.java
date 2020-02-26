package com.upgrade.backendchallenge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.upgrade.backendchallenge.dto.DayAvailabilityDTO;
import com.upgrade.backendchallenge.exception.ReservationException;
import com.upgrade.backendchallenge.model.DayAvailability;

public class DateUtils {
	
	public static Date addDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1); 
		return c.getTime();
	}
	
	public static Date addMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1); 
		return c.getTime();
	}
	
	public static Date getTodayDate() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
		return now.getTime();
	}
	
	public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public static Date parseDayAvailabilityToDate(DayAvailabilityDTO dayAvailability) {
		SimpleDateFormat format = new SimpleDateFormat(DayAvailability.ID_PATTERN);
		try {
			return format.parse(dayAvailability.getId());
		} catch (ParseException e) {
			throw new ReservationException("Id for the day availability is not valid",e);
		}
	}
	
	public static Date resetTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
