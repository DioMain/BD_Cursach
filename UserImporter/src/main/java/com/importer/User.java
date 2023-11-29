package com.importer;


public class User {

    public int USER_ID;

    public String USER_ROLE;

    public String NAME;
    public String SURNAME;
    public String PATRONYMIC;

    public String PHONE_NUMBER;
    public String PASSWORD;

    public String BIRTHDAY;
    public String EMAIL;

    public User()
    {
        USER_ID = 0;

        NAME = "";
        SURNAME = "";
        PATRONYMIC = "";

        PHONE_NUMBER = "";

        PASSWORD = "";

        BIRTHDAY = "";
        EMAIL = "";
        USER_ROLE = "";
    }
}
