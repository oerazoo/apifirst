package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProductsResourceTest {

    @Test
    void createAndGetProduct(){
        String id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Mouse\",\"price\":25.5,\"stock\":3}")
        .when().post("/products")
        .then().statusCode(201)
        .body("name", is("Mouse"))
        .body("id", notNullValue())
        .extract().path("id");

        assertDoesNotThrow(() -> java.util.UUID.fromString(id));

        given().when().get("/products/" + id)
                .then().statusCode(200).body("stock", is(3));
    }

    @Test
    void validateContractWhenInvalidData(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"X\",\"price\":-5}")
                .when().post("/products")
                .then().statusCode(400);
    }

    @Test
    void productNotFoundReturn404WithApiError() {
        given().when().get("/products/" + UUID.randomUUID())
                .then().statusCode(404)
                .body("code", is("PRODUCT_NOT_FOUND"));
    }

}