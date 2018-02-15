package org.example.chapter4.tests;

import fj.data.List;
import fj.data.Validation;
import org.example.chapter4.task.MatchPeople;
import org.example.chapter4.task.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatchPeopleTest {


    @Test
    public void testValidPositiveNumber() {

        assertTrue(MatchPeople.checkInt("as4").isFail());
        assertTrue(MatchPeople.checkInt("35").isSuccess());
    }

    @Test
    public void testValidString() {

        assertTrue(MatchPeople.checkNotEmpty("").isFail());
        assertTrue(MatchPeople.checkNotEmpty(null).isFail());
        assertTrue(MatchPeople.checkNotEmpty("asdsa").isSuccess());
    }

    @Test
    public void testAdult() {

        assertTrue(MatchPeople.checkAdult("20").isFail());
        assertTrue(MatchPeople.checkAdult("21").isSuccess());
    }

    @Test
    public void testGender() {

        assertTrue(MatchPeople.checkGender("MALE").isSuccess());
        assertTrue(MatchPeople.checkGender("FEMALE").isSuccess());
        assertTrue(MatchPeople.checkGender("OTHER").isSuccess());
        assertTrue(MatchPeople.checkAdult("Male").isFail());
    }

    @Test
    public void testCreatePerson() {

        assertTrue(MatchPeople.createPerson("1", "Angel", "33", "MALE").isSuccess());
        assertTrue(MatchPeople.createPerson("1", "Angel", "19", "MALE").isFail());
    }

    @Test
    public void testMatch() {

        Validation<List<String>, Person> personV = MatchPeople.createPerson("1", "Angel", "33", "MALE");
        Validation<List<String>, Person> person2V = MatchPeople.createPerson("1", "Angela", "33", "FEMALE");
        Validation<List<String>, Person> person3V = MatchPeople.createPerson("1", "Angelo", "33", "OTHER");

        assertEquals("Persons ids: " + personV.success().id + " " + person2V.success().id, MatchPeople.checkMatch(personV, person2V).head().head());
        assertEquals("Not matching genders", MatchPeople.checkMatch(personV, personV).head().head());
        assertEquals("Not matching genders", MatchPeople.checkMatch(personV, person3V).head().head());
    }
}
