import base.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;

import model.User;
import client.UserGenerator;
import org.junit.Assert;
import org.junit.Test;

public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Создаём пользователя")
    public void createUniqueUserPositiveTest() {
        User user = UserGenerator.getRandom();

        // Создаю пользователя и проверяю стату скод ответа (жду 200)
        ValidatableResponse createUser = userClient.createUser(user);
        int statusCode = createUser.extract().statusCode();
        // Получаю е-мейл пользователя и проверяю правильность е-мейла
        String email = createUser.extract().path("user.email");
        accessToken = createUser.extract().path("accessToken");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertEquals(user.getEmail().toLowerCase(), email);
    }

    @Test
    @DisplayName("Создаём идентичного пользователя")
    public void createUserWhoIsAlreadyRegisteredNegativeTest() {
        User user = new User(faker.name().firstName() + faker.number().digits(3) + "@yandex.ru", faker.number().digits(8) + faker.name().firstName(), faker.name().firstName());

        // Создаю пользователя и проверяю стату скод ответа (жду 200)
        ValidatableResponse createUser = userClient.createUser(user);
        int statusCode = createUser.extract().statusCode();
        accessToken = createUser.extract().path("accessToken");

        // Создаю второго идентичного пользователя и проверяю статус код и тело ответа
        ValidatableResponse createUser2 = userClient.createUser(user);
        int statusCode2 = createUser2.extract().statusCode();

        boolean success = createUser2.extract().path("success");
        String message = createUser2.extract().path("message");

        Assert.assertEquals(HTTP_FORBIDDEN, statusCode2);
        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertEquals(false, success);
        Assert.assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Создаём пользователя без эл. почты")
    public void createUserWithoutEmailNegativeTest() {
        User user = new User("", faker.number().digits(10), faker.name().firstName());

        ValidatableResponse createUser = userClient.createUser(user);
        int statusCode = createUser.extract().statusCode();
        boolean success = createUser.extract().path("success");
        String message = createUser.extract().path("message");

        Assert.assertEquals(false, success);
        Assert.assertEquals(HTTP_FORBIDDEN, statusCode);
        Assert.assertEquals("Email, password and name are required fields", message);

        if(statusCode == 200) {
            accessToken = createUser.extract().path("accessToken");
        }
    }

    @Test
    @DisplayName("Создаём пользователя без пароля")
    public void createUserWithoutPasswordNegativeTest() {
        User user = new User(faker.funnyName() + faker.number().digits(3) + "@yandex.ru", "", faker.name().firstName());

        ValidatableResponse createUser = userClient.createUser(user);
        int statusCode = createUser.extract().statusCode();
        boolean success = createUser.extract().path("success");
        String message = createUser.extract().path("message");

        Assert.assertEquals(HTTP_FORBIDDEN, statusCode);
        Assert.assertEquals(false, success);
        Assert.assertEquals("Email, password and name are required fields", message);

        if(statusCode == 200) {
            accessToken = createUser.extract().path("accessToken");
        }
    }

    @Test
    @DisplayName("Создаём пользователя без имени")
    public void createUserWithoutNameNegativeTest() {
        User user = new User(faker.funnyName() + faker.number().digits(3) + "@yandex.ru", faker.number().digits(10), "");

        ValidatableResponse createUser = userClient.createUser(user);
        int statusCode = createUser.extract().statusCode();
        boolean success = createUser.extract().path("success");
        String message = createUser.extract().path("message");

        Assert.assertEquals(HTTP_FORBIDDEN, statusCode);
        Assert.assertEquals(false, success);
        Assert.assertEquals("Email, password and name are required fields", message);

        if(statusCode == 200) {
            accessToken = createUser.extract().path("accessToken");
        }
    }
}