Feature: test graphql end point

  Background:
    * url baseUrl

  Scenario: simple graphql request
    # note the use of text instead of def since this is NOT json
    Given text query =
    """
{
    bookById(id: "book-2"){
        id
        name
        pageCount
        author {
            firstName
            lastName
        }
    }
}
    """
    And request { query: '#(query)' }
    When method post
    Then status 200

    # pretty print the response
    * print 'response:', response

    # json-path makes it easy to focus only on the parts you are interested in
    # which is especially useful for graph-ql as responses tend to be heavily nested
    # '$' happens to be a JsonPath-friendly short-cut for the 'response' variable
    #* match $.data.user.posts.data[0] contains { id: '1' }
    * match $.data.bookById.name == 'Moby Dick'
    # the '..' wildcard is useful for traversing deeply nested parts of the json
  #  * def posts = $..posts.data[*]
 #   * match posts[1] == { id: '2', title: 'qui est esse' }

  Scenario: graphql query from a file and using variables
    # here the query is read from a file
    Given def query = read("file:src/test/java/queries/book.graphql")
    And request { query: '#(query)' }
    When method post
    Then status 200

    And match $.data.bookById.name == "Harry Potter and the Philosopher's Stone"
    And match $.data.bookById.pageCount == 223
    And match $.data.bookById.author.lastName == 'Rowling'

    * print 'response:', response