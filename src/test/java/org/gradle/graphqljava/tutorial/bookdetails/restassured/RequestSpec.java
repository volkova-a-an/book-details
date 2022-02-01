package org.gradle.graphqljava.tutorial.bookdetails.restassured;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestSpec {
    static String BASE_URI = "http://localhost:8081/graphql/";

    public static RequestSpecification getReqSpec() {
       return given()
                .contentType(ContentType.JSON);
    }
}

