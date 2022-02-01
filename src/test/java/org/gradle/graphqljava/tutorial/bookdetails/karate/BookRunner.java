package org.gradle.graphqljava.tutorial.bookdetails.karate;

import com.intuit.karate.junit5.Karate;

class BookRunner {

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }
}