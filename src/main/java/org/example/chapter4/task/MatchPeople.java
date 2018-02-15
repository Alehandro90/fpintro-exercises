package org.example.chapter4.task;

import fj.data.List;
import fj.data.Validation;

import java.util.Scanner;

import static fj.data.Validation.fail;
import static fj.data.Validation.success;

public class MatchPeople {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Validation<List<String>, Person> person = unsafeReadPerson(scanner);

        Validation<List<String>, Person> person2 = unsafeReadPerson(scanner);

        System.out.println(checkMatch(person, person2));

    }

    public static List<List<String>> checkMatch(Validation<List<String>, Person> person, Validation<List<String>, Person> person2) {

        Validation<List<List<String>>, Validation<String, Match>> accumulateV = person.accumulate(person2, MatchPeople::match);

        if (accumulateV.isFail()) {
            return accumulateV.fail();
        }

        if (accumulateV.success().isFail()) {
            return List.single(List.single(accumulateV.success().fail()));
        }

        return List.single(List.single("Persons ids: " + accumulateV.success().success().id1 + " " + accumulateV.success().success().id2));

    }

    public static Validation<List<String>, Person> createPerson(String idString, String nameString, String ageString, String genderString) {

        Validation<String, Integer> idV = checkInt(idString);
        Validation<String, String> nameV = checkNotEmpty(nameString);
        Validation<String, Integer> ageV = checkAdult(ageString);
        Validation<String, Gender> genderV = checkGender(genderString);


//        Validation<String, Person> personV = idV.bind(id ->
//                nameV.bind(name ->
//                        ageV.bind(age ->
//                                genderV.map(gender -> new Person(id, name, age, gender)))));

        Validation<List<String>, Person> personV = idV.accumulate(
                nameV, ageV, genderV,
                Person::new

        );

        return personV;

    }

    public static Validation<String, String> checkNotEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return fail("Empty argument");
        }

        return success(str);
    }

    public static Validation<String, Integer> checkInt(String str) {

        Validation<String, String> stringV = checkNotEmpty(str);

        Validation<String, Integer> map = stringV.bind(x -> {
            try {
                return success(Integer.parseInt(x));
            } catch (NumberFormatException e) {
                return fail(e.getMessage() + " for argument " + str);
            }
        });

        return map;
    }

    public static Validation<String, Integer> checkAdult(String str) {

        Validation<String, Integer> integerV = checkInt(str);

        return integerV.bind(x -> x < 21 ? fail("Too young age: " + str) : success(x));
    }

    public static Validation<String, Gender> checkGender(String str) {

        Validation<String, String> stringV = checkNotEmpty((str));

        return stringV.bind(x -> {
            try {
                return success(Gender.valueOf(x));
            } catch (IllegalArgumentException e) {
                return fail(e.getMessage() + " for gender ");
            }
        });
    }

    static Validation<String, Match> match(Person person1, Person person2) {

        if (!person1.gender.equals(person2.gender) && !person1.gender.equals(Gender.OTHER) && !person2.gender.equals(Gender.OTHER)) {
            return success(new Match(person1.id, person2.id));
        }

        return fail("Not matching genders");
    }

    private static String unsafeReadConsole(Scanner sc, String msg) {
        System.out.println(msg);
        return sc.nextLine();
    }

    private static Validation<List<String>, Person> unsafeReadPerson(Scanner s) {
        return createPerson(
                unsafeReadConsole(s, "id"),
                unsafeReadConsole(s, "name"),
                unsafeReadConsole(s, "age"),
                unsafeReadConsole(s, "gender")
        );
    }
}
