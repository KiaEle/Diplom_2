import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import base.SetUpParamsTest;
import model.User;
import client.UserCredentional;
import client.UserGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class UpdateUserTest extends SetUpParamsTest {
    private String accessToken; // Токен первого пользователя
    private String accessToken2; // Токен второго пользователя

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией (изменён е-мейл)")
    public void changingUserDataWithAuthorizationEmailPositiveTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse authorizationUser = userClient.authorization(user, accessToken);

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateEmailCred(user, faker.name().firstName() + faker.number().digits(3) + "@yandex.ru"), accessToken);
        int statusCode = updateUser.extract().statusCode();

        boolean success = updateUser.extract().path("success");
        String newUserEmail = updateUser.extract().path("user.email");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertTrue(success);
        Assert.assertEquals(user.getEmail().toLowerCase(), newUserEmail);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией (изменён пароль)")
    public void changingUserDataWithAuthorizationPasswordPositiveTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse authorizationUser = userClient.authorization(user, accessToken);

        ValidatableResponse updateUser = userClient.update(UserCredentional.updatePasswordCred(user,  faker.number().digits(10)), accessToken);
        int statusCode = updateUser.extract().statusCode();
        boolean success = updateUser.extract().path("success");
        String newUserEmail = updateUser.extract().path("user.email");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertTrue(success);
        Assert.assertEquals(user.getEmail().toLowerCase(), newUserEmail);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией (изменено имя)")
    public void changingUserDataWithAuthorizationNamePositiveTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse authorizationUser = userClient.authorization(user, accessToken);
        int statusCode = authorizationUser.extract().statusCode();

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateNameCred(user,  faker.name().username()), accessToken);
        int statusCode2 = updateUser.extract().statusCode();
        boolean success = updateUser.extract().path("success");
        String newUserEmail = updateUser.extract().path("user.name");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertEquals(HTTP_OK, statusCode2);
        Assert.assertTrue(success);
        Assert.assertEquals(user.getName().toLowerCase(), newUserEmail);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации (изменено имя)")
    public void changingUserDataWithoutAuthorizationUpdateNameNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateNameCred(user,  faker.name().username()));
        int statusCode = updateUser.extract().statusCode();
        boolean success = updateUser.extract().path("success");
        String message = updateUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode);
        Assert.assertFalse(success);
        Assert.assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации (изменен пароль)")
    public void changingUserDataWithoutAuthorizationUpdatePasswordNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateEmailCred(user, faker.number().digits(10)));
        int statusCode = updateUser.extract().statusCode();

        boolean success = updateUser.extract().path("success");
        String message = updateUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode);
        Assert.assertFalse(success);
        Assert.assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации (изменен е-мейл)")
    public void changingUserDataWithoutAuthorizationUpdateEmailNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateEmailCred(user, faker.name().username() +  faker.number().digits(3) + "@yandex.ru"));
        int statusCode = updateUser.extract().statusCode();
        boolean success = updateUser.extract().path("success");
        String message = updateUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode);
        Assert.assertFalse(success);
        Assert.assertEquals("You should be authorised", message);
    }

    @Test
    @DisplayName("Изменение данных пользователя передаём уже существующую почту")
    public void changingUserDataTransferringExistingMailNegativeTest() {
        User user = UserGenerator.getRandom();
        User user2 = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        ValidatableResponse createUser2 = userClient.createUser(user2);

        accessToken = createUser.extract().path("accessToken");
        accessToken2 = createUser2.extract().path("accessToken");

        ValidatableResponse updateUser = userClient.update(UserCredentional.updateEmailCred(user, user2.getEmail()), accessToken);
        int statusCode = updateUser.extract().statusCode();


        boolean success = updateUser.extract().path("success");
        String message = updateUser.extract().path("message");

        Assert.assertEquals(HTTP_FORBIDDEN, statusCode);
        Assert.assertFalse(success);
        Assert.assertEquals("User with such email already exists", message);
    }

    @After
    public void clear() {
        if(accessToken != null) {
            ValidatableResponse deleteUser = userClient.deleteUser(accessToken);
            int statusCode = deleteUser.extract().statusCode();
            Assert.assertEquals(HTTP_ACCEPTED, statusCode);

            boolean success = deleteUser.extract().path("success");
            Assert.assertTrue(success);
        }

        if(accessToken2 != null) {
            ValidatableResponse deleteUser = userClient.deleteUser(accessToken2);
            int statusCode = deleteUser.extract().statusCode();
            Assert.assertEquals(HTTP_ACCEPTED, statusCode);

            boolean success = deleteUser.extract().path("success");
            Assert.assertTrue(success);
        }
    }
}