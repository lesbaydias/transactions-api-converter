package com.example.transactionservice;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.service.LimitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TransactionserviceApplicationTests {
	@Autowired
	private LimitService limitService;

	@Test
	public void testSetNewLimit() {
		Limit limit = new Limit();
		limit.setLimitCurrencyShortName("USD");
		limit.setLimitSum(new BigDecimal("1000"));

		Limit savedLimit = limitService.setNewLimit(limit);

		assertNotNull(savedLimit);
		assertEquals("USD", savedLimit.getLimitCurrencyShortName());
	}

}
