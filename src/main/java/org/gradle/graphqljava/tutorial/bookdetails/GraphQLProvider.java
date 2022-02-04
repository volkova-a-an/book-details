package org.gradle.graphqljava.tutorial.bookdetails;

import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * Once we have schema.graphql file we need to "bring it to life" by reading the file and parsing it and adding code to fetch data for it.
 * We create a new GraphQLProvider class in the package com.graphqljava.tutorial.bookdetails with an init method which will create a GraphQL instance:
 */
@Component
public class GraphQLProvider {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    // creates a GraphQL instance
    @PostConstruct
    // We use Guava Resources to read the file from our classpath, then create a GraphQLSchema and GraphQL instance.
    // This GraphQL instance is exposed as a Spring Bean via the graphQL() method annotated with @Bean.
    // The GraphQL Java Spring adapter will use that GraphQL instance to make our schema available via HTTP on the default url /graphql.
    //init method which will create a GraphQL instance:
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphql");
        String sdl = Resources.toString(url, StandardCharsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    // buildSchema method which creates the GraphQLSchema instance and wires in code to fetch data:
    private GraphQLSchema buildSchema(String sdl) {
        // TypeDefinitionRegistry is the parsed version of our schema file.
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();

        // SchemaGenerator combines the TypeDefinitionRegistry with RuntimeWiring to actually make the GraphQLSchema.
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    // buildWiring uses the graphQLDataFetchers bean to actually register two DataFetchers:
    // One to retrieve a book with a specific ID
    // One to get the author for a specific book.
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("allBooks", graphQLDataFetchers.getAllBooksDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createBook", graphQLDataFetchers.createBookDataFetcher()))
                .build();
    }

    //creates GraphQL instance

    /**
     * This GraphQL instance is exposed as a Spring Bean via the graphQL() method annotated with @Bean.
     * The GraphQL Java Spring adapter will use that GraphQL instance to make our schema available via HTTP on the url /graphql.
     * The URL is configured in application.properties and .graphqlconfig
     */

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }


}
