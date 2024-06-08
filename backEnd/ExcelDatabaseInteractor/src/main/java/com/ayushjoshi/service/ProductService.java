package com.ayushjoshi.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ayushjoshi.helper.Helper;
import com.ayushjoshi.model.Product;
import com.ayushjoshi.repo.ProductRepo;

@Service
public class ProductService {
	@Autowired
	public ProductRepo productRepo;

	public void saveExcelDataToDB(MultipartFile file) throws IOException {
		System.out.println("ProductService.saveExcelDataToDB()");
		List<Product> product = Helper.convertExcelToList(file.getInputStream());
		for (Product p : product) {
			System.out.println("p : " + p);
		}
		this.productRepo.saveAll(product);
		System.out.println("Data saved in DB");
	}

	public List<Product> getAllProducts() {
		return this.productRepo.findAll();
	}

	public ByteArrayInputStream getExcelDataFromDB() throws IOException {
		List<Product> products = productRepo.findAll();
		ByteArrayInputStream bais = Helper.dbDataToExcel(products);
		return bais;
	}
}
