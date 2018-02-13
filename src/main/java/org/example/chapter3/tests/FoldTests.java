package org.example.chapter3.tests;

import org.example.chapter3.ConsList;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class FoldTests
{
    private static int NUM = 5;
    private ConsList<Integer> EMPTY;
    private ConsList<Integer> ONE_ELEM;

    @Before
    public void init()
    {
        EMPTY = ConsList.empty();
        ONE_ELEM = ConsList.cons(NUM, ConsList.empty());
    }

    @Test
    public void isEmptyOnEmptyList()
    {
        assertTrue(EMPTY.isEmpty());
    }

    @Test
    public void isEmptyOnNonEmptyList()
    {
        assertFalse(ONE_ELEM.isEmpty());
    }

    @Test
    public void lengthOnEmptyList()
    {
        assertEquals(0, EMPTY.length());
    }

    @Test
    public void lengthOnNonEmptyList()
    {
        ConsList<Integer> myList = ConsList.cons(NUM, ConsList.cons(NUM, ConsList.empty()));

        assertEquals(2, myList.length());
    }

    @Test
    public void append()
    {
        ConsList<Integer> myList = EMPTY.append(NUM).append(NUM);


        assertFalse(myList.isEmpty());
        assertEquals(2, myList.length());
        assertEquals(myList.head().longValue(), NUM);
        assertEquals(myList.tail().head().longValue(), NUM);
    }

    @Test
    public void reverse()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.empty())));
        ConsList<Integer> myListReversed = ConsList.cons(3, ConsList.cons(2, ConsList.cons(1, ConsList.empty())));

        ConsList<Integer> result = myListReversed.reverse();
        assertEquals(myList, myListReversed.reverse());
    }

    @Test
    public void reverseOnEmptyList()
    {
        assertEquals(EMPTY, EMPTY.reverse());
    }

    @Test
    public void takeWhile()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.takeWhile(x -> x <= 2);
        ConsList<Integer> expectedList = ConsList.cons(1, ConsList.cons(2, ConsList.empty()));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void takeWhileOnEmptyList()
    {
        assertEquals(EMPTY, EMPTY.takeWhile(x -> x <= 2));
    }

    @Test
    public void takeWhileOnFirstElem()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertEquals(myList.takeWhile(x -> x < 1), ConsList.empty());
    }

    @Test
    public void takeWhileOnAllElements()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertEquals(myList.takeWhile(x -> x > 0), myList);
    }

    @Test
    public void filter()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(4, ConsList.empty()))));
        ConsList<Integer> actualList = myList.filter(x -> x <= 2);
        ConsList<Integer> expectedList = ConsList.cons(1, ConsList.cons(2, ConsList.empty()));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void filterLeft() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(4, ConsList.empty()))));
        ConsList<Integer> actualList = myList.filterLeft(x -> x <= 2);
        ConsList<Integer> expectedList = ConsList.cons(1, ConsList.cons(2, ConsList.empty()));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void filterOnEmptyList()
    {
        assertEquals(EMPTY, EMPTY.filter(x -> x <= 2));
    }

    @Test
    public void map()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.map(x -> x * 3);
        ConsList<Integer> expectedList = ConsList.cons(3, ConsList.cons(6, ConsList.cons(9, ConsList.cons(3, ConsList.empty()))));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void mapLeft()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        ConsList<Integer> actualList = myList.mapLeft(x -> x * 3);
        ConsList<Integer> expectedList = ConsList.cons(3, ConsList.cons(6, ConsList.cons(9, ConsList.cons(3, ConsList.empty()))));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void mapOnEmptyList()
    {
        assertEquals(EMPTY, EMPTY.map(x -> x * 3));
    }

    @Test
    public void forAll() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertTrue(myList.forAll(x -> x < 4));
        assertFalse(myList.forAll(x -> x > 4));
        assertFalse(myList.forAll(x -> x > 2));
    }

    @Test
    public void forAllLeft() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertTrue(myList.forAllLeft(x -> x < 4));
        assertFalse(myList.forAllLeft(x -> x > 4));
        assertFalse(myList.forAllLeft(x -> x > 2));
    }

    @Test
    public void exists() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertTrue(myList.exists(x -> x < 4));
        assertFalse(myList.exists(x -> x == 5));
        assertTrue(myList.exists(x -> x == 2));
    }

    @Test
    public void existsLeft() {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));

        assertTrue(myList.existsLeft(x -> x < 4));
        assertFalse(myList.existsLeft(x -> x == 5));
        assertTrue(myList.existsLeft(x -> x == 2));
    }

    @Test
    public void foldRightC()
    {
        ConsList<Integer> myList = ConsList.cons(1, ConsList.cons(2, ConsList.cons(3, ConsList.cons(1, ConsList.empty()))));
        int expectedResult = myList.foldRight((x, y) -> x * y, 0);
        int actualResult = myList.foldRightC((x, y) -> x * y, 0);

        assertEquals(expectedResult, actualResult);
    }

}
