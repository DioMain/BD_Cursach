using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public class User
{
    public int USER_ID;

    public string USER_ROLE;

    public string NAME;
    public string SURNAME;
    public string PATRONYMIC;

    public string PASSWORD;

    public string BIRTHDAY;
    public string PHONE_NUMBER;
    public string EMAIL;

    public User()
    {
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