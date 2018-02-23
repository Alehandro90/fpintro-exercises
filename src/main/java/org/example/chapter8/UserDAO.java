package org.example.chapter8;

import com.novarto.sanedbc.core.ops.EffectOp;
import com.novarto.sanedbc.core.ops.SelectOp;
import com.novarto.sanedbc.core.ops.UpdateOp;
import fj.Unit;
import fj.control.db.DB;
import fj.data.List;
import fj.data.Option;

import static com.novarto.sanedbc.core.ops.DbOps.unique;

public final class UserDAO {

    private static final PasswordHasher HASHER = new PasswordHasher(25, 25, 1001);

    public static DB<Unit> createUsersTable() {
        //this specific table has auto-generated ID
        return new EffectOp("CREATE TABLE USERS (ID INTEGER PRIMARY KEY IDENTITY, NAME NVARCHAR(200) NOT NULL, EMAIL NVARCHAR(200) NOT NULL, PASSWORD NVARCHAR(200) NOT NULL)");
    }

    public static DB<Integer> insertUser(String name, String email, String password) {
        return new UpdateOp(
                "INSERT INTO USERS(NAME, EMAIL, PASSWORD) VALUES(?, ?, ?)",
                // bind the single parameter of the statement
                ps -> {
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, HASHER.toHash(password));
                }
        );
    }

    public static DB<Integer> updateUser(String name, int id) {
        return new UpdateOp(
                "UPDATE USERS SET NAME = ? WHERE ID = ?",
                // bind the single parameter of the statement
                ps -> {
                    ps.setString(1, name);
                    ps.setInt(2, id);
                }
        );
    }

    public static DB<Option<UserStuff>> selectUserById(int id)
    {

        //given a regular operation which returns an iterable:
        DB<List<UserStuff>> temp = new SelectOp.FjList<>(
                "SELECT NAME, EMAIL FROM USERS WHERE ID=?",
                ps -> ps.setInt(1, id),
                rs -> new UserStuff(id, rs.getString(1), rs.getString(2)));

        // using the unique() function,
        // we can convert it to an operation which expects at most one result, and returns that optional result:
        return unique(temp);
    }

    public static DB<Option<UserStuff>> selectUserByEmail(String email)
    {

        //given a regular operation which returns an iterable:
        DB<List<UserStuff>> temp = new SelectOp.FjList<>(
                "SELECT ID, NAME FROM USERS WHERE EMAIL=?",
                ps -> ps.setString(1, email),
                rs -> new UserStuff(rs.getInt(1), rs.getString(2), email));

        // using the unique() function,
        // we can convert it to an operation which expects at most one result, and returns that optional result:
        return unique(temp);
    }

    public static DB<Boolean> verifyCredits(String email, String password) {
        //given a regular operation which returns an iterable:
        DB<List<String>> temp = new SelectOp.FjList<>(
                "SELECT PASSWORD FROM USERS WHERE EMAIL=?",
                ps -> ps.setString(1, email),
                rs -> rs.getString(1));

        // using the unique() function,
        // we can convert it to an operation which expects at most one result, and returns that optional result:
        return unique(temp).map(x ->
             x.isSome() ? HASHER.validatePassword(password, x.some()) : Boolean.FALSE
        );
    }

}
