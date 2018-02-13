package org.example;

public class Util
{
    public static <A> A notImplemented()
    {
        throw error("not implemented");
    }

    public static Error error(String error)
    {
        return new Error(error);
    }

    public static void println(Object o)
    {
        System.out.println(o);
    }
}
