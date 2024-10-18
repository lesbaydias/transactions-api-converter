package com.example.transactionservice;
import com.example.transactionservice.dto.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class TransactionserviceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testCreateTransaction() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestJson = """
            {
                "accountFrom": 1111111111,
                "accountTo": 2222222222,
                "currencyShortName": "KZT",
                "sum": 150000.00,
                "expenseCategoryType": "PRODUCT"
            }
        """;

		HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		ResponseEntity<TransactionResponse> response = restTemplate.exchange(
				"/api/transactions/create", HttpMethod.POST, entity, TransactionResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
	}
}
