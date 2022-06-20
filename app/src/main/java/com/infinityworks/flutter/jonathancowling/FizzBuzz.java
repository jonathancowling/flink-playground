package com.infinityworks.flutter.jonathancowling;

import org.apache.flink.api.common.functions.MapFunction;

public class FizzBuzz implements MapFunction<Long, String> {

    // normally fizzbuzz doesn't want the number printed in all cases,
    // but, as this is not necessarily ordered,
    // it's useful to see the number if not already included in the output a different way
    private final boolean includeValue;

    public FizzBuzz(boolean includeValue) {
        this.includeValue = includeValue;
    }

    public FizzBuzz() {
        this(true);
    }

    @Override
    public String map(Long value) throws Exception {
        boolean fizz = value % 3 == 0;
        boolean buzz = value % 5 == 0;

        StringBuilder b = new StringBuilder();

        if (includeValue) {
            b.append("(" + value.toString() + "): " );
        }
        if (fizz) {
            b.append("fizz");
        }
        if (buzz) {
            b.append("buzz");
        }
        if (!fizz && !buzz) {
            b.append(value.toString());
        }

        return b.toString();
    }
    
}
