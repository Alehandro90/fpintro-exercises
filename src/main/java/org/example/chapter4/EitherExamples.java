package org.example.chapter4;

import fj.data.Either;

import static fj.data.Either.left;
import static fj.data.Either.right;
import static org.example.Util.println;

public class EitherExamples
{
    public static void main(String[] args)
    {

        Either<String, Integer> e1 = left("shrubbery");
        Either<String, Integer> e2 = right(42);

        println(e1);
        println(e2);

        Either<Integer, String> ee1 = e1.bimap(x -> x.length(), x -> "The integer " + x);
        Either<Integer, String> ee2 = e2.bimap(x -> x.length(), x -> "The integer " + x);

        System.out.println(ee1);
        System.out.println(ee2);

        Integer cata1 = e1.either(str -> str.length(), num -> num + 5);
        Integer cata2 = e2.either(str -> str.length(), num -> num + 5);

        println(cata1);
        println(cata2);

        //Either<Order, OrderItem> thing = ...
        //BigDecimal amount = thing.either(order -> order.grossAmount, item -> item.price.multiply(item.quantity))

        // left projection lets us apply transformations to the left side, if it is left. Otherwise NO-OP
        Either.LeftProjection<String, Integer> lp = e1.left();
        Either<String, Integer> mapped = lp.map(x -> x + x);
        System.out.println(mapped);
        Either<String, Integer> mapped2 = e1.right().map(x -> x * 4);
        System.out.println(mapped2);

        //same for right
        Either<String, Integer> mapped3 = e2.right().map(x -> x - 20);
        System.out.println(mapped3);

        // bind a.k.a. flatMap
        // lets us apply a transformation where the result itself is an either

        Either<String, Short> bind = e2.right().bind(x -> {
            if (x >= Short.MIN_VALUE && x <= Short.MAX_VALUE)
            {
                return right((short)x.intValue());
            }
            else
            {
                return left("not in range: " + x);
            }
        });

        System.out.println(bind);

        // imperatively get a value out of an either

        if (e1.isLeft())
        {
            String str = e1.left().value(); // non-total, throws if not left
            System.out.println("left --> " + str);
        }
        else
        {
            Integer num = e1.right().value(); // non-total, throws if not right
            System.out.println(num);
        }

        // get a value out of an either, provided with a default
        String withDefault = e1.left().orValue(() -> "default");
        Integer intDef = e1.right().orValue(() -> 55);

        System.out.println(withDefault);
        System.out.println(intDef);

    }
}
