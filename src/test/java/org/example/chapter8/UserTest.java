package org.example.chapter8;

import com.novarto.sanedbc.core.interpreter.SyncDbInterpreter;
import com.novarto.sanedbc.core.ops.EffectOp;
import fj.data.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public final class UserTest {
    private final String nameA = "Aleks";
    private final String emailA = "aleks@gmail.com";
    private final String passwordA = "123";
    private final String nameP = "Peter";
    private final String fakePassword = "1234";

    private static SyncDbInterpreter dbi = new SyncDbInterpreter(
                () -> DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", ""
                ));

    @Before
    public void createTable() {
        dbi.submit(UserDAO.createUsersTable());
    }

    @Test
    public void testInsertUser() {


        Integer updateCount = dbi.submit(UserDAO.insertUser(nameA, emailA, passwordA));
        assertThat(updateCount, is(1));
    }

    @Test
    public void testSelectUserById() {
        dbi.submit(UserDAO.insertUser(nameA, emailA, passwordA));

        Option<UserStuff> user = dbi.submit(UserDAO.selectUserById(0));

        assertThat(user.some().name, is(nameA));
    }

    @Test
    public void testSelectUserByEmail() {
        dbi.submit(UserDAO.insertUser(nameA, emailA, passwordA));

        Option<UserStuff> user = dbi.submit(UserDAO.selectUserByEmail(emailA));

        assertThat(user.some().name, is(nameA));
    }

    @Test
    public void testUpdateUser() {
        dbi.submit(UserDAO.insertUser(nameA, emailA, passwordA));

        dbi.submit(UserDAO.updateUser(nameP, 0));

        Option<UserStuff> userP = dbi.submit(UserDAO.selectUserById(0));

        assertThat(userP.some().name, is(nameP));

    }

    @Test
    public void testVerifyCredits() {
        dbi.submit(UserDAO.insertUser(nameA, emailA, passwordA));

        Boolean verifyA = dbi.submit(UserDAO.verifyCredits(emailA, passwordA));
        Boolean verifyFakePassword = dbi.submit(UserDAO.verifyCredits(emailA, fakePassword));
        Boolean verifyFakeEmail = dbi.submit(UserDAO.verifyCredits(emailA + "1", passwordA));

        assertTrue(verifyA);
        assertFalse(verifyFakePassword);
        assertFalse(verifyFakeEmail);

    }

    @After
    public void dropUsersTable() {
        EffectOp drop_table_users_ = new EffectOp("DROP TABLE USERS ");
        dbi.submit(drop_table_users_);
    }

}
