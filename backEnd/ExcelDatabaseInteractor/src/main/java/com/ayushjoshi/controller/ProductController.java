package com.ayushjoshi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ayushjoshi.helper.Helper;
import com.ayushjoshi.model.Product;
import com.ayushjoshi.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("ProductController.uploadFile()");
        if (Helper.checkExcelFormat(file)) {
            this.productService.saveExcelDataToDB(file);
            return ResponseEntity.ok(Map.of("message", "File uploaded successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file...");
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping("/getExcelData")
//    public ResponseEntity<Resource> getExcelData() throws IOException {
//        ByteArrayInputStream bais = productService.getExcelDataFromDB();
//        InputStreamResource excelFile = new InputStreamResource(bais);
//        ResponseEntity<Resource> responseBody = ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename= " + Helper.SHEET_NAME)
//                .contentType(MediaType.parseMediaType(Helper.EXCEL_FILE_TYPE)).body(excelFile);
//        return responseBody;
//    }
    public ResponseEntity<Resource> getExcelData() throws IOException {
        // Retrieve Excel data from the database as a ByteArrayInputStream
        ByteArrayInputStream bais = productService.getExcelDataFromDB();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bytesRead;
        byte[] buffer = new byte[1024];
        try {
            while ((bytesRead = bais.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the byte array from the ByteArrayOutputStream
        byte[] byteArray = outputStream.toByteArray();
        
        // Wrap the byte array in a ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(byteArray);
        
        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + Helper.SHEET_NAME);
        headers.add(HttpHeaders.CONTENT_TYPE, Helper.EXCEL_FILE_TYPE);

        // Return ResponseEntity with ByteArrayResource and headers
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
