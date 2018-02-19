package org.example.chapter7;

import fj.P1;
import fj.Try;
import fj.data.List;
import fj.data.Validation;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TryExample
{

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {

        P1<Validation<Exception, String>> getFileContentsProgram = getFileContents("/farenheit.txt");

        P1<Validation<Exception, List<Double>>> toCelsiusProgram = getFileContentsProgram.map(v -> v.map(contents -> farenheit(contents)));

        // end of the world
        List<Double> result = new SyncInterpeter().run(toCelsiusProgram);
        System.out.println(result);


        ExecutorService ex = Executors.newCachedThreadPool();
        AsyncInterpreter asyncInterpreter = new AsyncInterpreter(ex);

        CompletableFuture<List<Double>> asyncResult = asyncInterpreter.run(toCelsiusProgram);

        List<Double> xs = asyncResult.get();


//        asyncInterpreter.run(getFileContents("blaf_nosuch")).get();



        ex.shutdown();

    }



    static List<Double> farenheit(String fileContents)
    {
        List<Double> farenheits = doubleRows(nonEmptyRows(fileContents));
        return farenheits.map(a -> toCelsius(a));
    }



    static double toCelsius(double farenheit)
    {
        return ((farenheit - 32) * 5) / 9;
    }



    static List<String> nonEmptyRows(String contents)
    {
        String[] xs = contents.split("\n");
        List<String> rows = List.arrayList(xs);
        return rows.map(String::trim).filter(x -> !x.isEmpty());
    }

    static List<Double> doubleRows(List<String> rows)
    {
        List<Validation<NumberFormatException, Double>> xs = rows.map(x -> {

            P1<Validation<NumberFormatException, Double>> res = Try.f(() -> Double.parseDouble(x));
            return res.f();
        });

        List<Double> res = xs.filter(a -> a.isSuccess()).map(a -> a.success());
        return res;
    }



    static P1<Validation<Exception, String>> getFileContents(String pathString)
    {
        P1<Validation<Exception, Path>> getPath = Try.f(() -> unsafeToPath(pathString));

        P1<Validation<Exception, String>> res = getPath.map(pathV -> {

            Validation<Exception, String> getContents = pathV.bind(path -> Try.f(() -> unsafeReadFile(path)).f());
            return getContents;

        });

        return res;
    }


    //side-effecting procedures

    private static String unsafeReadFile(Path p) throws Exception
    {
        System.out.println("entering unsafeReadFile");
        return new String(
                Files.readAllBytes(p), StandardCharsets.UTF_8
        );
    }

    private static Path unsafeToPath(String cpPath) throws Exception
    {
        System.out.println("entering unsafeToPath");
        URL res = TryExample.class.getResource(cpPath);

        if(res == null)
        {
            throw new IllegalArgumentException("no such resource: " + cpPath);
        }

        return Paths.get(res.toURI());
    }

    static class SyncInterpeter
    {
        public <A> A run(P1<Validation<Exception, A>> program)
        {
            return program.f().success();
        }
    }

    static class AsyncInterpreter
    {
        private final ExecutorService ex;

        public AsyncInterpreter(ExecutorService ex)
        {
            this.ex = ex;
        }

        public <A> CompletableFuture<A> run(P1<Validation<Exception, A>> program)
        {
            return CompletableFuture.supplyAsync(
                    () -> program.f().success()
            );
        }
    }


}
