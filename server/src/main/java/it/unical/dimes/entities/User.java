package it.unical.dimes.entities;

import java.util.Objects;

public class User {

    private int id;
    private String username;

    public User(String username){
        this.username = username;
    }

    public User(int id, String username){
        this.id = id;
        this.username = username;
    }

    public User(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if( this == o ) return true;
        if( ! (o instanceof User)) return false;
        User user = (User) o;
        return id==user.id && Objects.equals(username,user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,username);
    }
}
