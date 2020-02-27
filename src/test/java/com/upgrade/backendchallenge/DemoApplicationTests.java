package com.upgrade.backendchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.model.DayAvailability;
import com.upgrade.backendchallenge.util.DateUtils;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class DemoApplicationTests {

	private static final int RESERVATION_THREADS = 200;
	private static final String HOST = "http://localhost:";
	private static final String RESERVATION_ENDPOINT = "/reservation/";
	private static final String AVAILABILITY_ENDPOINT = "/availability/";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final int MAX_OCCUPANCY = 15;
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testBasicCreateAndRetrieveReservation() throws Exception {
		ReservationDTO dto = new ReservationDTO();
		dto.setEmail("test@gmail.com");
		dto.setFullName("Fuller Namaste");
		Date d1 = DateUtils.addDay(new Date());
		dto.setStartDate(d1);
		Date d2 = DateUtils.addDay(DateUtils.addDay(d1));
		dto.setEndDate(d2);
		dto.setNumberOfPeople(1);
		
		HttpEntity<ReservationDTO> entity = new HttpEntity<ReservationDTO>(dto,headers);
		ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort(RESERVATION_ENDPOINT), entity, String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		
		String reservationId = response.getBody();
		
		HttpEntity<String> entityRetrieve = new HttpEntity<String>(null, headers);
		response = restTemplate.exchange(
				createURLWithPort(RESERVATION_ENDPOINT+reservationId), HttpMethod.GET, entityRetrieve,
				String.class);
		JSONObject body = new JSONObject(response.getBody());
		assertEquals(reservationId,body.get("id").toString());
	}

	@Test
    public void testMultithreadedReservationCreation() throws InterruptedException 
    {
        TestUtil.runMultithreaded( new Runnable() {
            public void run() {
                try{
                	HttpEntity<ReservationDTO> entity = new HttpEntity<ReservationDTO>(createRandomDto(), headers);
            		ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort(RESERVATION_ENDPOINT), entity, String.class);
            		if (response.getStatusCode().is2xxSuccessful())
            			System.out.println(ANSI_GREEN+response+ANSI_RESET);
            		else
            			System.out.println(ANSI_RED+response+ANSI_RESET);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        , RESERVATION_THREADS);
        
    }
	
	@Test
	public void testOccupancyLimit() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort(AVAILABILITY_ENDPOINT))
		        .queryParam("startDate", DayAvailability.getDayAvailavilityId(DateUtils.addDay(new Date())))
		        .queryParam("endDate", DayAvailability.getDayAvailavilityId(DateUtils.addMonth(new Date())));
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<List<DayAvailability>> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, 
				new ParameterizedTypeReference<List<DayAvailability>>() {
        });
		for (DayAvailability dayAvailability : response.getBody()) {
			assertTrue(dayAvailability.getOccupancy()<=MAX_OCCUPANCY);
		}
	}

	/**
	 * create url for requests
	 * @param uri
	 * @return
	 */
	private String createURLWithPort(String uri) {
		return HOST + port + uri;
	}

	/**
	 * Create random reservation
	 * @return
	 */
	public static ReservationDTO createRandomDto() {
		ReservationDTO dto = new ReservationDTO();
		dto.setEmail(DemoApplicationTests.randomString());
		dto.setFullName(DemoApplicationTests.randomString());
		Date d1 = DateUtils.addDay(DemoApplicationTests.randomDate());
		dto.setStartDate(d1);
		Date d2 = DateUtils.addDay(DateUtils.addDay(d1));
		dto.setEndDate(d2);
		dto.setNumberOfPeople(ThreadLocalRandom.current().nextInt(1, 3 + 1));
		return dto;
	}
	
	/**
	 * Create random string for email and name
	 * @return
	 */
	public static String randomString() {
	    byte[] array = new byte[7];
	    new Random().nextBytes(array);
	    return new String(array, Charset.forName("UTF-8"));
	}
	
	
	/**
	 * Create random Date for reservation. On purpose date can be out of scope for reservation restrictions.
	 * @return
	 */
	public static Date randomDate() {
		return new Date(ThreadLocalRandom.current().nextLong((DateUtils.addDay(new Date())).getTime(), (DateUtils.addMonth(new Date())).getTime()));
	}
	
}
