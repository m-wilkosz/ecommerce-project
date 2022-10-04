package com.wilkosz.ecommerceapp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertAll;

public class RestApiTests {

    @Test
    public void checkoutController_purchase200() throws FileNotFoundException {

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

    @Test
    public void checkoutController_purchase415() {

        RestAssured.baseURI = "https://localhost:8443/api/checkout";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        request.header("Content-Type", "application/xml");
        Response response = request.post("/purchase");

        assertAll (
                () -> Assertions.assertEquals(415, response.getStatusCode())
        );
    }

    @Test
    public void checkoutController_paymentIntent200() throws FileNotFoundException {

        RestAssured.baseURI = "https://localhost:8443/api/checkout";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/paymentInfo.json");
        Scanner scan = new Scanner(is).useDelimiter("\\A");
        String jsonString = scan.hasNext() ? scan.next() : "";

        request.header("Content-Type", "application/json");
        request.body(jsonString);
        Response response = request.post("/payment-intent");

        System.out.println(response.getBody().asString());

        assertAll (
                () -> Assertions.assertEquals(200, response.getStatusCode()),
                () -> Assertions.assertTrue(response.getBody().asString().contains("\"amount\": 99")),
                () -> Assertions.assertTrue(response.getBody().asString().contains("\"currency\": \"usd\"")),
                () -> Assertions.assertTrue(response.getBody().asString()
                        .contains("\"receipt_email\": \"jkowalski@gmail.com\""))
        );
    }

    @Test
    public void getStates200() {

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/states?size=225");
        int statesNumber = response.getBody().jsonPath().getList("_embedded.states.id").size();

        assertAll (
                () -> Assertions.assertEquals(200, response.getStatusCode()),
                () -> Assertions.assertEquals(223, statesNumber)
        );
    }

    @Test
    public void getProductCategories200() {

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/product-category");
        int productCategoriesNumber = response.getBody().jsonPath().getList("_embedded.productCategory.id").size();

        assertAll (
                () -> Assertions.assertEquals(200, response.getStatusCode()),
                () -> Assertions.assertEquals(4, productCategoriesNumber)
        );
    }

    @Test
    public void getProducts200() {

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/products?size=105");
        int productsNumber = response.getBody().jsonPath().getList("_embedded.products.id").size();

        assertAll (
                () -> Assertions.assertEquals(200, response.getStatusCode()),
                () -> Assertions.assertEquals(100, productsNumber)
        );
    }

    @Test
    public void getCountries200() {

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/countries?size=10");
        int countriesNumber = response.getBody().jsonPath().getList("_embedded.countries.id").size();

        assertAll (
                () -> Assertions.assertEquals(200, response.getStatusCode()),
                () -> Assertions.assertEquals(6, countriesNumber)
        );
    }

    @Test
    public void getOrdersOfSpecificCustomer401() {

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/orders/search/findByCustomerEmailOrderByDateCreatedDesc?email=jkowalski@gmail.com");

        assertAll (
                () -> Assertions.assertEquals(401, response.getStatusCode())
        );
    }

    /*@Test
    public void getOrdersOfSpecificCustomer200() throws FileNotFoundException {

        RestAssured.baseURI = "https://dev-72782908.okta.com";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/oktaUser.json");
        Scanner scan = new Scanner(is).useDelimiter("\\A");
        String jsonString = scan.hasNext() ? scan.next() : "";

        request.header("Content-Type", "application/json");
        request.body(jsonString);
        Response sessionTokenResponse = request.post("/api/v1/authn");
        String sessionToken = sessionTokenResponse.getBody().jsonPath().get("sessionToken").toString();

        request = RestAssured.given().relaxedHTTPSValidation();
        Response authResponse = request
                .get("/oauth2/v1/authorize?client_id=0oa69nl8v5ubRu6hi5d7&response_type=code&redirect_uri=" +
                        "https://localhost:4200/login/callback&code_challenge=wvdBk56lkrgnps-oa_iO05I35SZMcVUWI6SCezgUkWE" +
                        "&code_challenge_method=S256&response_mode=form_post&scope=openid" +
                        "&state=ZRNrwp3op2Xv8XqaqjPFHivPP9YUsvH8GyrL3D0CPw3gXyxLaKAhzvZSCdkXlVWb&sessionToken=" + sessionToken);

        //TODO https://support.okta.com/help/s/article/How-to-get-tokens-for-an-OIDC-application-without-a-browser-using-curl-Postman?language=en_US

        RestAssured.baseURI = "https://localhost:8443/api";

        RequestSpecification request = RestAssured.given().relaxedHTTPSValidation();

        Response response = request.get("/orders/search/findByCustomerEmailOrderByDateCreatedDesc?email=jkowalski@gmail.com");
        int customerOrdersNumber = response.getBody().jsonPath().getList("_embedded.orders.id").size();

        assertAll (
                () -> Assertions.assertTrue(sessionTokenResponse.getBody().asString().contains("sessionToken"))
        );
    }*/
}