package com.graphqljava.tutorial.bookdetails.restassured;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.graphqljava.tutorial.bookdetails.Book;
import com.graphqljava.tutorial.bookdetails.GraphQLQuery;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.graphqljava.tutorial.bookdetails.restassured.RequestSpec.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredTest {

    @Test
    public void getBookNameByIdTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Book id = new Book();
        id.setId("book-1");

        GraphQLQuery query = new GraphQLQuery();

        String file = Files.readString(Path.of("src/main/resources/book.graphql"));

        query.setQuery(file);

        given(RequestSpec.getReqSpec())
                .body(query)
                .when().post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .body("data.bookById.name", equalTo("Harry Potter and the Philosopher's Stone"));
    }
}
