package org.example.chapter6;

import fj.*;
import fj.data.List;
import fj.data.Stream;
import fj.function.Effect0;
import fj.function.Effect1;
import fj.function.Try0;
import fj.function.TryEffect0;

import static fj.P.lazy;
import static org.example.Util.println;

public class Strictness
{

    public static void main(String[] args)
    {

        boolean x = true;
        boolean y = false;

        println(x || forever());
        println(x || never());

        println(y && forever());
        println(y && never());

        println(uAnd(() -> y, Strictness::forever));
        println(uOr(() -> x, Strictness::never));


//        withResource(() -> 42, res -> {
//            throw new LinkageError("BOOM!!!");
//        }, res -> {
//            System.out.println("cleaned up resource");
//        });

        int xxxx = uIf(() -> true, () -> 42, () -> 43);

        println(uIf(() -> true, () -> 42, Strictness::never));

        Stream<Integer> integers = allInts();

        Stream<Integer> xs = integers.filter(i -> i % 3 == 0).filter(i -> i > 200).take(5);
        System.out.println(xs.toList());



        F1W<Integer, Integer> ff = F1W.lift(Strictness::bar);
        F1W<Integer, Integer> mapped = ff.map(a -> a + 3);
        System.out.println(mapped.f(2));

        P1<Integer> foo = lazy(Strictness::foo);
        P1<Integer> twice = foo.map(a -> a * 2);
        Integer res = twice.f();
        System.out.println(res);

        P1<Integer> memo = foo.hardMemo();
        println(memo.f());
        println(memo.f());

        P1<Integer> twice_ = memo.map(a -> a * 2);
        println(twice_.f());
        println(twice_.f());

        System.out.println();

    }


    public static Stream<Integer> allInts()
    {
        return ints(0);
    }

    private static Stream<Integer> ints(Integer from)
    {
        return Stream.cons(from, () -> ints(from + 1));
    }

    private static boolean forever()
    {
        while (true)
        {
        }
    }

    private static boolean never()
    {
        throw new RuntimeException("oh noez");
    }


    public static boolean uAnd(F0<Boolean> x, F0<Boolean> y)
    {
        return x.f() ? y.f() : false;
    }

    public static boolean uOr(F0<Boolean> x, F0<Boolean> y)
    {
        return x.f() ? true : y.f();
    }

    public static <A> A uIf(F0<Boolean> cond, F0<A> ifExpr, F0<A> elExpr) {
        return cond.f() ? ifExpr.f() : elExpr.f();
    }

    public static int bar(int x)
    {
        return x*2;
    }


    public static <R, A> A withResource(F0<R> acquire, F<R, A> code, Effect1<R> e)
    {

        R res = acquire.f();
        try
        {
            return code.f(res);
        }
        finally
        {
            e.f(res);
        }
    }

    public static int foo()
    {
        System.out.println("computing");
        return 42;
    }

}
