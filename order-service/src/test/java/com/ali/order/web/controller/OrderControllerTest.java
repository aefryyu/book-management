package com.ali.order.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.ali.order.AbstractIT;
import com.ali.order.testdata.TestDataFactory;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderNested {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.0"));
            var payload =
                    """
                            {
                              "customer": {
                                "name": "intan",
                                "email": "intan@email.com",
                                "phone": "09987"
                              },
                              "deliveryAddress": {
                                "addressLine1": "jalan1",
                                "addressLine2": "jalan2",
                                "city": "jakarta",
                                "state": "ruya",
                                "zipCode": "9987",
                                "country": "USA"
                              },
                              "items": [
                                {
                                  "code": "P100",
                                  "name": "Product 1",
                                  "price": 25.0,
                                  "quantity": 1
                                }
                              ]
                            }

                            """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
