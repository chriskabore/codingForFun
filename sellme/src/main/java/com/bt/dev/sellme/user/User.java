package com.bt.dev.sellme.user;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

    @Id @GeneratedValue
    private Integer userId;

    private String name;

    private String password;

    private @ElementCollection(fetch = FetchType.EAGER)
    List<String> roles;

    protected User(){}

    public User(Integer userId, String name, String password, List<String> roles){
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }


    public User(String name, String password, List<String> roles){
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(password, user.password)
                && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, password, roles);
    }

    @Override
    public String toString() {
        return "User{" + "id='" + userId + '\'' + ", name='" + name + '\'' + ", password='" + "*******" + '\'' + ", roles="
                + roles + '}';
    }

}
