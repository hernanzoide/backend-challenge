package com.upgrade.backendchallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.upgrade.backendchallenge.dto.ReservationDTO;
import com.upgrade.backendchallenge.util.DateUtils;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	private static final String HOST = "http://localhost:";
	private static final String ENDPOINT = "/reservation/";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RESET = "\u001B[0m";
	
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
		ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort(ENDPOINT), entity, String.class);
		assertTrue(response.getStatusCode().is2xxSuccessful());
		
		String reservationId = response.getBody();
		
		HttpEntity<String> entityRetrieve = new HttpEntity<String>(null, headers);
		response = restTemplate.exchange(
				createURLWithPort(ENDPOINT+reservationId), HttpMethod.GET, entityRetrieve,
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
            		ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort(ENDPOINT), entity, String.class);
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
        , 100);
    }
	
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
