package org.example.chapter8;

import java.util.Objects;

public class UserStuff {
    public final int id;
    public final String name;
    public final String email;

    public UserStuff(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStuff userStuff = (UserStuff) o;
        return id == userStuff.id &&
                Objects.equals(name, userStuff.name) &&
                Objects.equals(email, userStuff.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, email);
    }
}
