package org.example.chapter4.task;

import fj.F;
import fj.P;
import fj.P2;
import fj.data.Either;
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

        Validation<List<String>, Match> matchingObjV = checkMatch(person, person2);

        if(matchingObjV.isSuccess()) {
            System.out.println("Persons ids: " + matchingObjV.success().id1 + " " + matchingObjV.success().id2);
        } else {
            System.out.println(matchingObjV.fail());
        }

    }

    public static Validation<List<String>, Match>  checkMatch(Validation<List<String>, Person> person, Validation<List<String>, Person> person2) {

        Validation<List<List<String>>, P2<Person, Person>> tmp = person.accumulate(person2, (p1, p2) -> P.p(p1, p2));

        Validation<List<String>, P2<Person, Person>> productV = leftMap(tmp, xxs -> List.join(xxs));

        return productV.bind(p -> leftMap(match(p._1(), p._2()), single -> List.single(single)));


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
                return fail(e.getMessage());
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
                return fail(e.getMessage());
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

    public static <E, A, E1> Validation<E1, A> leftMap(Validation<E, A> v, F<E, E1> errTransform)
    {
        Either<E, A> either = v.toEither();
        Either<E1, A> mapped = either.left().map(errTransform);
        return Validation.validation(mapped);
    }

}
