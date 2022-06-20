package com.infinityworks.flutter.jonathancowling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FizzBuzzTest {    
    
    @ParameterizedTest
    @CsvSource({
        "1,(1): 1",
        "2,(2): 2",
        "3,(3): fizz",
        "4,(4): 4",
        "5,(5): buzz",
        "15,(15): fizzbuzz",
        // edge cases
        "0,(0): fizzbuzz",
        "-1,(-1): -1",
        "-2,(-2): -2",
        "-3,(-3): fizz",
        "-4,(-4): -4",
        "-5,(-5): buzz",
        "-15,(-15): fizzbuzz",
    })
    public void fizzBuzzShouldReturnCorrectAnswers(String input, String expected) throws Exception {
        assertEquals(expected, new FizzBuzz().map(Long.valueOf(input)));
    }


}
