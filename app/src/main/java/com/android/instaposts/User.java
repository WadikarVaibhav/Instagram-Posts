package com.android.instaposts;

public class User {

    private final String name;
    private final String nickname;
    private final String email;
    private final String id;

    public User() {
        this.name = "";
        this.nickname = "";
        this.email = "";
        this.id = "";
    }

    public User(String name, String nickname, String email, String id) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

}
