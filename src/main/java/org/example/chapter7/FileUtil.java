package org.example.chapter7;

import fj.P1;
import fj.Unit;
import fj.data.List;
import fj.data.Validation;

import java.nio.file.Path;

import static org.example.Util.notImplemented;

public class FileUtil
{

    /**
     * Read a file
     */
    public static P1<Validation<Exception, String>> readFile(Path absolutePath)
    {
        return notImplemented();
    }

    /**
     *
     * Write a file. If file already exists and is folder, fail. If file aready exists and is file and force=false, fail
     */
    public static P1<Validation<Exception, Unit>> writeFile(Path absolutePath, String contents, boolean force)
    {
        return notImplemented();
    }

    /**
     * Delete a file. If file does not exist, fail
     */
    public static P1<Validation<Exception, Unit>> delete(Path absolutePath)
    {
        return notImplemented();
    }

    /**
     * List the files and folders which are direct descendants of this path
     * If path does not exist, fail.
     */
    public static P1<Validation<Exception, List<String>>> ls(Path absolutePath)
    {
        return notImplemented();

    }
}
