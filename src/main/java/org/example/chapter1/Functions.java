package org.example.chapter1;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.example.Util.notImplemented;

public class Functions
{

    public static <A> Function<A, A> identity()
    {
        return notImplemented();
    }


    public static <A, B, C> Function<A, C> compose(Function<A, B> f, Function<B, C> g)
    {
        return notImplemented();
    }

    public static <A, B, C> Function<A, Function<B, C>>  curry(BiFunction<A, B, C> f)
    {
        return notImplemented();
    }

    public static <A, B> Function<A, B> constt(B b)
    {
        return notImplemented();
    }

    public static <A, B,C> BiFunction<A, B, C> uncurry(Function<A, Function<B, C>> f)
    {
        return notImplemented();
    }


}
