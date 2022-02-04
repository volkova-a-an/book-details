package org.gradle.graphqljava.tutorial.bookdetails;

import lombok.Data;

@Data
public class GraphQLQuery {
    private String query;
    private Object variables;
}
