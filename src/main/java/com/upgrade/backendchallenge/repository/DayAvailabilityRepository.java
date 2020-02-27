package com.upgrade.backendchallenge.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.upgrade.backendchallenge.model.DayAvailability;

@Repository
public interface DayAvailabilityRepository extends CrudRepository<DayAvailability, Long> {

	List<DayAvailability> findByDayBetween(String startDate, String endDate);
}
