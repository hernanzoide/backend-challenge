package com.upgrade.backendchallenge.repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.AllDocsResponse;
import com.upgrade.backendchallenge.exception.ReservationException;
import com.upgrade.backendchallenge.model.DayAvailability;

@Component
public class DayAvailabilityRepository {
	
	private static final String DATABASE_NAME = "day-availabilities";
	
	private Database dayAvailabilityDatabase;
	
	@Autowired
	public DayAvailabilityRepository(CloudantClient cloudantClient) {
		this.dayAvailabilityDatabase = cloudantClient.database(DayAvailabilityRepository.DATABASE_NAME, true);
	}
	
	public void create(DayAvailability reservation) {
		Response response = this.dayAvailabilityDatabase.save(reservation);
		reservation.setId(response.getId());
		reservation.setRevision(response.getRev());
	}
	
	public DayAvailability get(LocalDate date) {
		return dayAvailabilityDatabase.find(DayAvailability.class,date.toString());
	}
	
	public DayAvailability get(String id) {
		return dayAvailabilityDatabase.find(DayAvailability.class,id);
	}
	
 	public boolean contains(LocalDate date) {
		return dayAvailabilityDatabase.contains(date.toString());
	}
 	
 	public boolean contains(String id) {
		return dayAvailabilityDatabase.contains(id);
	}
	
	public void edit(DayAvailability reservation) {
		this.dayAvailabilityDatabase.save(reservation);
	}
	
	public void delete(DayAvailability reservation) {
		this.dayAvailabilityDatabase.remove(reservation);
	}

	public List<DayAvailability> get(String startDate, String endDate) {
		
		AllDocsResponse response;
		try {
			response = this.dayAvailabilityDatabase.getAllDocsRequestBuilder().startKeyDocId(startDate)
					.endKeyDocId(endDate).includeDocs(true).build().getResponse();
			return response.getDocsAs(DayAvailability.class);
		} catch (IOException e) {
			throw new ReservationException("Can't reach database");
		}
	}
	
	public void createOrUpdateAll(List<DayAvailability> availabilities) {
		this.dayAvailabilityDatabase.bulk(availabilities);
	}
}

