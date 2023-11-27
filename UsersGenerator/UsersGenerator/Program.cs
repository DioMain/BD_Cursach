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
        string[] namePool = new string[] { "Дмитрий", "Александр", "Андрей", "Андройд", "Алексей", "Килир", "Богдан", "Антон", "Никита", "Олег" };
        string[] surnamePool = new string[] { "Окулич", "Метлушко", "Богданов", "Зыков", "Стадольник", "Еремин", "Хартанович", "Нестерович" };
        string[] patronymicPool = new string[] { "Дмитриевич", "Александрович", "Андреевич", "Кирилович", "Алексеевич", "Антонович", "Никитич", "Олегович" };
        string[] phoneNumberPool = new string[] { "+375298422939", "+375292612373", "+375297798079", "+375257733326", "+375255442652", "+375447621257", "+375292504668", "+375336339887" };

        string[] roles = new string[] { "PATIENT", "DOCTOR" };

        List<User> users = new List<User>();

        Random rand = new Random();

        //2004-04-16
        for (int i = 0; i < 100000; i++)
        {
            string date = $"{rand.Next(1985, 2017)}-{rand.Next(1, 13)}-{rand.Next(1, 31)}";

            User user = new User()
            {
                Name = namePool[rand.Next(namePool.Length)],
                Surname = surnamePool[rand.Next(surnamePool.Length)],
                Patronymic = patronymicPool[rand.Next(patronymicPool.Length)],

                PhoneNumber = phoneNumberPool[rand.Next(phoneNumberPool.Length)],
                Role = roles[rand.Next(roles.Length)],

                Birthday = date,

                Password = "1111",
                Email = $"someemail.{i}@email.com"
            };

            users.Add(user);
        }

        string result = JsonSerializer.Serialize(users, options);

        File.WriteAllText("../../../../../USERS.json", result);
    }
}