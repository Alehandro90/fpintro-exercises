package org.example.chapter7;

import fj.P1;
import fj.Unit;
import fj.data.Validation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FileUtilTest {
    private static final String DIR = "src/main/resources";
    private static final String FILEPATH = DIR + "/testFile.txt";
    private static final String CONTENT = "some input";
    private static final String DIRECTORY_ERROR_MSSG = "This is directory not a file!";
    private static final String FORCE_FALSE_MSSG = "Force is set to false!";
    private static final String NO_SUCH_FILE_ERR_MSSG = "File does not exist!";
    private static final String FAKE_DIR = "No such directory!";

    @Before
    public void setUp() throws IOException {

        FileUtil.writeFile(Paths.get(FILEPATH), CONTENT, true).f();
    }


    @Test
    public void testReadExistingFile() throws IOException {

        assertTrue(FileUtil.readFile(Paths.get(FILEPATH)).f().success().equals(CONTENT));

    }

    @Test
    public void testReadNotExistingFile() throws IOException {

        Files.deleteIfExists(Paths.get(FILEPATH));

        assertTrue(FileUtil.readFile(Paths.get(FILEPATH)).f().isFail());

    }

    @Test
    public void testWriteNotExistingWhenGivenFileWithoutForce() throws IOException {

        Files.deleteIfExists(Paths.get(FILEPATH));

        P1<Validation<Exception, Unit>> validationFile = FileUtil.writeFile(Paths.get(FILEPATH), CONTENT, false);

        assertTrue(validationFile.f().isSuccess());

    }

    @Test
    public void testWriteNotExistingWhenGivenFileWithForce() throws IOException {
        Files.deleteIfExists(Paths.get(FILEPATH));

        P1<Validation<Exception, Unit>> validationFileWithForce = FileUtil.writeFile(Paths.get(FILEPATH), CONTENT, true);
        assertTrue(validationFileWithForce.f().isSuccess());
    }

    @Test
    public void testWriteExistingWhenGivenFileWithForce() {

        P1<Validation<Exception, Unit>> validationFileWithForce = FileUtil.writeFile(Paths.get(FILEPATH), CONTENT, true);
        assertTrue(validationFileWithForce.f().isSuccess());
    }

    @Test
    public void testWriteExistingWhenGivenFileWithoutForce() {

        P1<Validation<Exception, Unit>> validationFile = FileUtil.writeFile(Paths.get(FILEPATH), CONTENT, false);
        assertTrue(validationFile.f().fail().getMessage().equals(FORCE_FALSE_MSSG));
    }

    @Test
    public void testWriteFileWhenGivenDirectory() {
        P1<Validation<Exception, Unit>> validationFile = FileUtil.writeFile(Paths.get(DIR), CONTENT, false);

        assertTrue(validationFile.f().fail().getMessage().equals(DIRECTORY_ERROR_MSSG));
    }

    @Test
    public void testDeleteExistingFile() {
        assertTrue(FileUtil.delete(Paths.get(FILEPATH)).f().isSuccess());
        assertFalse(Files.exists(Paths.get(FILEPATH)));
    }

    @Test
    public void testDeleteNotExistingFile() throws IOException {
        Files.deleteIfExists(Paths.get(FILEPATH));

        assertTrue(FileUtil.delete(Paths.get(FILEPATH)).f().fail().getMessage().equals(NO_SUCH_FILE_ERR_MSSG));
    }

    @Test
    public void testLsWithExistingPath() throws IOException {

        assertTrue(FileUtil.ls(Paths.get(DIR)).f().success().isNotEmpty());
    }

    @Test
    public void testLsWithNotExistingPath() throws IOException {

        assertTrue(FileUtil.ls(Paths.get(FILEPATH)).f().fail().getMessage().equals(FAKE_DIR));
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(FILEPATH));
    }
}
