package com.graphqljava.tutorial.bookdetails;

import lombok.Data;

@Data    //Lombok annotation for creating getters and setters
public class GraphQLQuery {
    private String query;
    private Object variables;
}
