package org.example.chapter7;

import fj.P1;

import java.util.Scanner;

import static fj.P.lazy;

public class ReadFromConsole
{

    public static void main(String[] args)
    {
        P1<Integer> len1 = readConsole().map(String::length);
        P1<Integer> len2 = readConsole().map(String::length);

        P1<Integer> maxLen = len1.bind(x -> len2.map(y -> Math.max(x, y)));

        // no side effects so far. We do that only "at the end of the world:"

        Integer max = maxLen.f();

        System.out.println("max length:" + max);

    }

    private static P1<String> readConsole()
    {
        return lazy(ReadFromConsole::unsafeReadConsole);
    }


    private static String unsafeReadConsole()
    {
        System.out.println("enter something and press enter:");
        return new Scanner(System.in).nextLine();
    }
}
