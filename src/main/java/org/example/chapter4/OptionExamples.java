package org.example.chapter4;

import fj.data.List;
import fj.data.Option;

import static fj.data.Option.none;
import static fj.data.Option.some;
import static org.example.Util.println;

public class OptionExamples
{

    public static void main(String[] args)
    {
        //construct an optional value from a value
        Option<String> x = some("shrubbery");
        println(x);
        //construct an empty optional value
        Option<Integer> y = none();
        println(y);
        //check if an option is defined or not
        println(x.isSome());
        println(x.isNone());

        //transform a value if defined
        Option<Integer> length = x.map(xx -> xx.length());
        println(length);

        //transform a value, where result is also optional
        Option<Character> firstChar = x.bind(xx -> xx.length() > 0 ? some(xx.charAt(0)) : none());
        println(firstChar);

        //produce a regular value from an optional value, given a default
        String str = x.orSome("alabala");
        println(str);

        Integer i = y.orSome(42);
        println(i);

        //produce a regular value from an optional value, given a default, lazily
        String str2 = x.orSome(() -> {
            System.out.println("will not be printed");
            return "alabala";
        });
        System.out.println(str2);


        // treat an option as a list of zero or one elements
        List<String> xs = x.toList();
        System.out.println(xs);

        List<Integer> ys = y.toList();
        System.out.println(ys);

    }
}
