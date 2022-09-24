package com.wilkosz.ecommerceapp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class EcommerceAppApplicationTests {

	@Test
	public void placeOrderTest() throws FileNotFoundException {

		RestAssured.baseURI = "https://localhost:8443/api/checkout";

		RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

		InputStream is = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/purchase.json");
		Scanner scan = new Scanner(is).useDelimiter("\\A");
		String jsonString = scan.hasNext() ? scan.next() : "";

		request.header("Content-Type", "application/json");
		request.body(jsonString);
		Response response = request.post("/purchase");

		assertAll (
				() -> Assertions.assertEquals(200, response.getStatusCode()),
				() -> Assertions.assertTrue(response.getBody().asString().contains("orderTrackingNumber"))
		);
	}
}