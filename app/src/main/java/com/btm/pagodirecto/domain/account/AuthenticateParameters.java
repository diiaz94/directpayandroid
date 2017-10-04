package com.btm.pagodirecto.domain.account;

/**
 * Created by diiaz94 on 04-06-2017.
 */
public class AuthenticateParameters {
    String email;
    String password;

    public AuthenticateParameters(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
