package org.example.chapter1.tests;

import org.example.chapter1.Functions;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public final class FunctionsTest {

    public static final Function<Integer, Integer> COMPOSE_FUNC_A = a -> a;
    public static final Function<Integer, Integer> COMPOSE_FUNC_B = b -> b * 2;

    public static final BiFunction<Integer, Integer, Integer> CURRY_FUNCTION = (a, b) -> a + b;
    public static final Function<Integer, Integer> CONSTANT_FUNCTION = b -> b;

    public static final Function<Integer, Function<Integer, Integer>> UNCURRY_FUNCTION = a -> (b -> a + b);

    @Test
    public void composeFunctions() {

        assertEquals(12, Functions.compose(COMPOSE_FUNC_A, COMPOSE_FUNC_B).apply(6).intValue());
    }

    @Test
    public void curryFunctions() {

    assertEquals(6, Functions.curry(CURRY_FUNCTION).apply(2).apply(4).intValue());
    }

    @Test
    public void identityFunctions() {

        assertEquals(7, Functions.identity().apply(7));
    }

    @Test
    public void constFunction() {

        assertEquals(CONSTANT_FUNCTION, Functions.constt(CONSTANT_FUNCTION).apply(5));
    }

    @Test
    public void uncurryFunction() {

        assertEquals(9, Functions.uncurry(UNCURRY_FUNCTION).apply(5, 4).intValue());
    }
}
