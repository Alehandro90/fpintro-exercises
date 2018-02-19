package org.example.chapter5;

import fj.data.Validation;

import static fj.data.Validation.fail;
import static fj.data.Validation.success;

public class ValidationExample
{

    public static void main(String[] args)
    {
        System.out.println(halfSum("asdf", "14"));
        System.out.println(halfSum("14", "15"));
        System.out.println(halfSum("1", "1"));
        System.out.println(halfSum("", null));



    }



    static Validation<String, Integer> halfSum(String arg1, String arg2)
    {

        Validation<String, Integer> num1V = isDefined(arg1).bind(x -> parseInt(x));
        Validation<String, Integer> num2V = isDefined(arg2).bind(x -> parseInt(x));

        Validation<String, Integer> sumV = num1V.bind(num1 -> num2V.map(num2 -> num1 + num2));

        return  sumV.bind(sum -> isEven(sum).map(evenSum -> evenSum / 2));

    }

    static Validation<String, String> isDefined(String str)
    {
        if (str == null || str.isEmpty())
        {
            return fail("NOT_DEFINED");
        }
        return success(str);
    }

    static Validation<String, Integer> parseInt(String str)
    {
        try
        {
            return success(Integer.parseInt(str));
        }
        catch (NumberFormatException e)
        {
            return fail(e.getMessage());
        }
    }

    static Validation<String, Integer> isEven(Integer x)
    {
        if (x % 2 == 0)
        {
            return success(x);
        }

        return fail("NOT_EVEN");
    }

}
