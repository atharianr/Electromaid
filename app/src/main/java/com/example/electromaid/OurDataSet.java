package com.example.electromaid;

public class OurDataSet {
    String name;
    String email;
    String password;
    json json;

    public com.example.electromaid.json getJson() {
        return json;
    }

    public void setJson(com.example.electromaid.json json) {
        this.json = json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OurDataSet() {
    }

    public OurDataSet(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
