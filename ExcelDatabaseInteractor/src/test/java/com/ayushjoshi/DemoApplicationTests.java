package com.ayushjoshi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayushjoshi.repo.ProductRepo;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ProductRepo productRepo;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void testProductRepo() {
		System.out.println("Product Repo correctly injected....");
	}
	
	@Test
	void testDataFromProductRepo() {
		productRepo.findAll().forEach(p->System.out.println(p.getProductName()));
	}

}
