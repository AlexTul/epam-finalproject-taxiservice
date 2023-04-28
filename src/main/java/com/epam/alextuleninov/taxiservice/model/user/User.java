package com.epam.alextuleninov.taxiservice.model.user;

import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class for User entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class User implements Comparable {

    private final long id;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String password;

    private final Role role;

    private User(long id, String firstName, String lastName,
                String email, String password, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User create(long id, String firstName, String lastName,
                               String email, String password, Role role) {
        return new User(id, firstName, lastName, email, password, role);
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getEmail().compareTo(((User) o).getEmail());
    }

    public static class UserBuilder {

        private long id;

        private String firstName;

        private String lastName;

        private String email;

        private String password;

        private Role role;

        public UserBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return User.create(id, firstName, lastName, email, password, role);
        }
    }
}
