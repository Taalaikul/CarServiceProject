package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	private static final Long VEHICLE_ID = Long.valueOf(100);

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PriceRepository priceRepository;

	@Test
	public void shouldGeneratePriceForGivenId(){

		priceRepository.save(new Price("USD", BigDecimal.valueOf(900000), VEHICLE_ID));
		Price testPrice = priceRepository.findById(VEHICLE_ID).get();

		ResponseEntity<Price>response = this.restTemplate.getForEntity("http://localhost:"+port+"/prices/"+ VEHICLE_ID, Price.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getCurrency(), testPrice.getCurrency());
		assertEquals(response.getBody().getPrice(), testPrice.getPrice());

		priceRepository.deleteById(VEHICLE_ID);
	}

//	@Test
//	public void contextLoads() {
//	}

}
