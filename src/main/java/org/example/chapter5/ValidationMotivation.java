package org.example.chapter5;

import fj.data.Either;

import static fj.data.Either.left;
import static fj.data.Either.right;

public class ValidationMotivation
{


    static Either<String, Integer> halfSum(String arg1, String arg2)
    {

        Either<String, Integer> num1E = isDefined(arg1).right().bind(x -> parseInt(x));
        Either<String, Integer> num2E = isDefined(arg2).right().bind(x -> parseInt(x));

        Either<String, Integer> sumE = num1E.right().bind(num1 -> num2E.right().map(num2 -> num1 + num2));

        return  sumE.right().bind(sum -> isEven(sum).right().map(evenSum -> evenSum / 2));

    }

    static Either<String, String> isDefined(String str)
    {
        if (str == null || str.isEmpty())
        {
            return left("NOT_DEFINED");
        }
        return right(str);
    }

    static Either<String, Integer> parseInt(String str)
    {
        try
        {
            return right(Integer.parseInt(str));
        }
        catch (NumberFormatException e)
        {
            return left(e.getMessage());
        }
    }

    static Either<String, Integer> isEven(Integer x)
    {
        if (x % 2 == 0)
        {
            return right(x);
        }

        return left("NOT_EVEN");
    }
}
