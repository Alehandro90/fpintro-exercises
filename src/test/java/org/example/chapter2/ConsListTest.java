package org.example.chapter2;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class ConsListTest {

    private static int NUM = 5;
    private static Function<Integer, Boolean> PREDICATE = x -> x <= 2;
    private static Function<Integer, Integer> FUNCTION = x -> x * 3;

    private ConsList<Integer> EMPTY;
    private ConsList<Integer> ONE_ELEM;

    @Before
    public void init() {
        EMPTY = ConsList.empty();
        ONE_ELEM = ConsList.cons(NUM, ConsList.empty());
    }

    @Test
    public void isEmptyOnEmptyList() {
        assertTrue(EMPTY.isEmpty());
    }

    @Test
    public void isEmptyOnNonEmptyList() {
        assertFalse(ONE_ELEM.isEmpty());
    }

    @Test
    public void lengthOnEmptyList() {
        assertEquals(0, EMPTY.length());
    }

    @Test
    public void lengthOnNonEmptyList() {
        ConsList<Integer> myList = ConsList.cons(NUM, ConsList.cons(NUM, ConsList.empty()));

        assertEquals(2, myList.length());
    }

    @Test
    public void append() {
        ConsList<Integer> myList = EMPTY.append(NUM).append(NUM);


        assertFalse(myList.isEmpty());
        assertEquals(2, myList.length());
        assertEquals(myList.head().longValue(), NUM);
        assertEquals(myList.tail().head().longValue(), NUM);
    }

    @Test
    public void reverse() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.empty())));
        ConsList<Integer> myListReversed = ConsList.cons(3, ConsList.cons(2, ConsList.cons(1, ConsList.empty())));

        ConsList<Integer> result = myListReversed.reverse();
        assertEquals(myList, myListReversed.reverse());
    }

    @Test
    public void reverseOnEmptyList() {
        assertEquals(EMPTY, EMPTY.reverse());
    }

    @Test
    public void takeWhile() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.takeWhile(PREDICATE);
        ConsList<Integer> expectedList = ConsList.cons(1, ConsList.cons(2, ConsList.empty()));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void takeWhileOnEmptyList() {
        assertEquals(EMPTY, EMPTY.takeWhile(PREDICATE));
    }

    @Test
    public void takeWhileOnFirstElem() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertEquals(myList.takeWhile(x -> x < 1), ConsList.empty());
    }

    @Test
    public void takeWhileOnAllElements() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertEquals(myList.takeWhile(x -> x > 0), myList);
    }

    @Test
    public void dropWhile() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.dropWhile(PREDICATE);
        ConsList<Integer> expectedList = ConsList.cons(3, ConsList.cons(1, ConsList.empty()));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void dropWhileOnEmptyList() {
        assertEquals(EMPTY, EMPTY.dropWhile(PREDICATE));
    }

    @Test
    public void filter() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.filter(PREDICATE);
        ConsList<Integer> expectedList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(1, ConsList.empty())));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void filterOnEmptyList() {
        assertEquals(EMPTY, EMPTY.filter(PREDICATE));
    }

    @Test
    public void map() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.map(FUNCTION);
        ConsList<Integer> expectedList = ConsList.cons(3, ConsList.cons(6, ConsList.cons(9, ConsList.cons(3, ConsList.empty()))));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void mapOnEmptyList() {
        assertEquals(EMPTY, EMPTY.map(FUNCTION));
    }

}