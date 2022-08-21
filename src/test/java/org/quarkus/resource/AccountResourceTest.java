package org.quarkus.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.quarkus.entities.Account;
import org.quarkus.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Predicates.equalTo;
import static groovy.xml.Entity.not;
import static io.restassured.RestAssured.given;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AccountResourceTest {
    @Test
    @Order(1)
    void testRetrieveAll(){
        Response result =
                given()
                        .when().get("/accounts")
                        .then().statusCode(200)
                        .body(
                                containsString("George Baird"),
                                containsString("Mary Taylor"),
                                containsString("Diana Rigg")
                        ).extract()
                        .response();

        var accounts = result.jsonPath().getList("$");
        assertFalse(accounts.isEmpty());
        assertThat(accounts, hasSize(4));
    }
    @Test
    @Order(2)
    void testGetAccount() {
        Account account =
                given()
                        .when().get("/accounts/{accountNumber}", 545454545)
          .then()
                .statusCode(200)
                .extract()
                .as(Account.class);
        assertEquals(account.getAccountNumber(),545454545L);
        assertEquals(account.getCustomerName(),"Diana Rigg");
        assertEquals(account.getBalance(), new BigDecimal("422.00"));
        assertEquals(account.getAccountStatus(), (AccountStatus.OPEN));
    }

    @Test
    @Order(3)
    void testCreateAccount() {
        Account newAccount = new Account(324324L, 112244L, "Sandy Holmes", new BigDecimal("154.55"));

        Account returnedAccount =
                given()
                        .contentType(ContentType.JSON)
      .body(newAccount)
      .when().post("/accounts")
      .then()
                .statusCode(201)
        .extract()
                .as(Account.class);

        assertNotNull(returnedAccount);
        assertEquals(returnedAccount, newAccount);

        Response result =
                given()
                        .when().get("/accounts")
      .then()
                .statusCode(200)
                .body(
                        containsString("George Baird"),
                        containsString("Mary Taylor"),
                        containsString("Diana Rigg"),
                        containsString("Sandy Holmes")
                )
                .extract()
                .response();

        List<Account> accounts = result.jsonPath().getList("$");
        assertFalse(accounts.isEmpty());
        assertThat(accounts, hasSize(4));
    }
}
