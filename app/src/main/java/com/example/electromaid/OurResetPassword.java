package com.example.electromaid;

public class OurResetPassword {
    private String reset_token;
    private String password;

    public OurResetPassword(String password, String reset_token) {
        this.password = password;
        this.reset_token = reset_token;
    }
}
