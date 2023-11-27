using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public class User
{
    public string Name;
    public string Surname;
    public string Patronymic;

    public string PhoneNumber;
    public string Password;

    public string Birthday;
    public string Email;

    public string Role;

    public User()
    {
        Name = "";
        Surname = "";
        Patronymic = "";

        PhoneNumber = "";

        Password = "";

        Birthday = "";
        Email = "";
        Role = "";
    }
}