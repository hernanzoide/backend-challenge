package com.upgrade.backendchallenge.rest;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upgrade.backendchallenge.dto.DayAvailabilityDTO;
import com.upgrade.backendchallenge.model.DayAvailability;
import com.upgrade.backendchallenge.service.DayAvailabilityService;

@RestController
@RequestMapping("/availability/")
public class AvailabilityController {
	
	@Autowired
	private DayAvailabilityService dayAvailabilityService;
	
	@GetMapping("/")
    public ResponseEntity<List<DayAvailabilityDTO>> get(@RequestParam("startDate") @DateTimeFormat(pattern=DayAvailability.ID_PATTERN) Date startDate,
    		@RequestParam("endDate") @DateTimeFormat(pattern=DayAvailability.ID_PATTERN) Date endDate) {
		
		List<DayAvailabilityDTO> availabilities = new ArrayList<DayAvailabilityDTO>();

		this.dayAvailabilityService.get(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
			.stream().forEach(da -> availabilities.add(new DayAvailabilityDTO(da)));
		return new ResponseEntity<List<DayAvailabilityDTO>>(availabilities, HttpStatus.OK);
	}

}
