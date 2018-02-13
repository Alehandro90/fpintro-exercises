package org.example.chapter3;

import static org.example.chapter3.ConsList.cons;
import static org.example.chapter3.ConsList.empty;

public class Example
{
    public static void main(String[] args)
    {

        System.out.println(sum(cons(1, cons(2, cons(3, cons(4, empty()))))));


        System.out.println(product(cons(1, cons(2, cons(3, cons(4, empty()))))));

        System.out.println(concat(cons(1, cons(2, empty()))));


    }

    private static int sum(ConsList<Integer> xs)
    {
        return xs.foldLeft((x, y) -> x+y, 0);
    }

    private static int product(ConsList<Integer> xs)
    {
        return xs.foldLeft((x, y) -> x * y, 1);
    }

    private static <A> String concat(ConsList<A> xs)
    {
        return xs.foldLeft((x, y) -> x.toString() + y.toString(), "");
    }

}
