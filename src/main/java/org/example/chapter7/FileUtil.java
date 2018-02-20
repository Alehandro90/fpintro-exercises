package org.example.chapter7;

import fj.P1;
import fj.Try;
import fj.Unit;
import fj.data.List;
import fj.data.Validation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static fj.Unit.unit;
import static java.nio.file.Files.deleteIfExists;

public class FileUtil {

    /**
     * Read a file
     */
    public static P1<Validation<Exception, String>> readFile(Path absolutePath) {
        return Try.f(() -> unsafeReadFile(absolutePath));
    }

    /**
     * Write a file. If file already exists and is folder, fail. If file aready exists and is file and force=false, fail
     */
    public static P1<Validation<Exception, Unit>> writeFile(Path absolutePath, String contents, boolean force) {
        return Try.f(() -> writeToFile(absolutePath, contents, force));
    }

    /**
     * Delete a file. If file does not exist, fail
     */
    public static P1<Validation<Exception, Unit>> delete(Path absolutePath) {
        return Try.f(() -> unsafeDeleteFile(absolutePath));
    }

    /**
     * List the files and folders which are direct descendants of this path
     * If path does not exist, fail.
     */
    public static P1<Validation<Exception, List<String>>> ls(Path absolutePath) {
        return Try.f(() -> unsafeLs(absolutePath));

    }

    private static String unsafeReadFile(Path p) throws IOException {
        return new String(
                Files.readAllBytes(p), StandardCharsets.UTF_8
        );
    }

    private static Unit writeToFile(Path absolutePath, String contents, boolean force) throws Exception {

        if (Files.isDirectory(absolutePath)) {
            throw new IllegalArgumentException("This is directory not a file!");
        }

        if (Files.exists(absolutePath)) {
            if (force) {
                return unsafeWriteToFile(absolutePath, contents);
            }
            throw new IllegalArgumentException("Force is set to false!");
        }

        return unsafeWriteToFile(absolutePath, contents);

    }

    private static List<String> unsafeLs(Path absolutePath) throws IOException {
        if (!Files.exists(absolutePath) || !Files.isDirectory(absolutePath)) {
            throw new IllegalArgumentException("No such directory!");
        }

        List<String> listFiles = List.nil();

        java.util.List<String> elements = Files.list(absolutePath).map(x -> x.toString()).collect(Collectors.toList());

        for(String element : elements) {
            listFiles = listFiles.cons(element);
        }

        return listFiles;

    }

    private static Unit unsafeDeleteFile(Path absolutePath) throws IOException {
        if (deleteIfExists(absolutePath)) {
            return unit();
        }

        throw new NoSuchFileException("File does not exist!");
    }

    private static Unit unsafeWriteToFile(Path absolutePath, String contents) throws IOException {

        Files.write(absolutePath, contents.getBytes("utf-8"));

        return unit();
    }

}
