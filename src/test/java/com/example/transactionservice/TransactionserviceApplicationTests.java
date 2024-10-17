package com.example.transactionservice;

import com.example.transactionservice.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TransactionserviceApplicationTests {
	@Autowired
	private LimitService limitService;

}
