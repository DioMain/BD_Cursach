using System.Text;
using System.Text.Encodings.Web;
using System.Text.Unicode;
using System.Text.Json;

public class Program
{
    public static JsonSerializerOptions options;

    static Program()
    {
        options = new JsonSerializerOptions()
        {
            IncludeFields = true,
            WriteIndented = true,
        };
    }

    public static void Main()
    {
        string[] namePool = new string[] { "Дмитрий", "Александр", "Андрей", "Андройд", "Алексей", "Килир", "Богдан", "Антон", "Никита", "Олег", "Владимир", "Владислав" };
        string[] surnamePool = new string[] { "Окулич", "Метлушко", "Богданов", "Зыков", "Стадольник", "Еремин", "Хартанович", "Нестерович", "Козоловский", "Булко" };
        string[] patronymicPool = new string[] { "Дмитриевич", "Александрович", "Андреевич", "Кирилович", "Алексеевич", "Антонович", "Никитич", "Олегович", "Юрьевич", "Владимирович", "Владиславович" };
        string[] phoneNumberPool = new string[] { "+375298422939", "+375292612373", "+375297798079", "+375257733326", "+375255442652", "+375447621257", "+375292504668", "+375336339887" };

        string[] roles = new string[] { "PATIENT", "DOCTOR" };

        List<User> users = new List<User>();

        Random rand = new Random();

        //2004-04-16
        for (int i = 0; i < 100000; i++)
        {
            string date = $"{rand.Next(1985, 2017)}-{rand.Next(1, 13)}-{rand.Next(1, 29)}";

            User user = new User()
            {
                USER_ID = i + 100000,
                NAME = namePool[rand.Next(namePool.Length)],
                SURNAME = surnamePool[rand.Next(surnamePool.Length)],
                PATRONYMIC = patronymicPool[rand.Next(patronymicPool.Length)],

                PHONE_NUMBER = phoneNumberPool[rand.Next(phoneNumberPool.Length)],
                USER_ROLE = roles[rand.Next(roles.Length)],

                BIRTHDAY = date,

                PASSWORD = "1111",
                EMAIL = $"someemail.{i}@email.com"
            };

            users.Add(user);
        }

        File.WriteAllText("../../../../../IMPORT.json", JsonSerializer.Serialize(users, options));
    }
}