import base.BaseTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import client.UserGenerator;
import org.junit.Assert;
import org.junit.Test;

import static java.net.HttpURLConnection.*;

public class LoginUserTest extends BaseTest {

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginAsExistingUserPositiveTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");
        String refreshToken = createUser.extract().path("refreshToken");

        ValidatableResponse authorizationUser = userClient.authorization(user, refreshToken);
        int statusCode2 = authorizationUser.extract().statusCode();

        boolean success = authorizationUser.extract().path("success");
        String userEmail = authorizationUser.extract().path("user.email");

        Assert.assertEquals(HTTP_OK, statusCode2);
        Assert.assertEquals(true, success);
        Assert.assertEquals(user.getEmail().toLowerCase(), userEmail);
    }

    @Test
    @DisplayName("Авторизация под не существующим пользователем")
    public void loginAsNotExistingUserNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse authorizationUser = userClient.authorization(user, faker.starTrek().character());
        int statusCode = authorizationUser.extract().statusCode();

        boolean success = authorizationUser.extract().path("success");
        String message = authorizationUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode);
        Assert.assertEquals(false, success);
        Assert.assertEquals("email or password are incorrect", message);

        if(statusCode == 200) {
            accessToken = authorizationUser.extract().path("accessToken");
        }
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void authorizationWthoutPasswordNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);

        accessToken = createUser.extract().path("accessToken");
        String refreshToken = createUser.extract().path("refreshToken");

        user.setPassword("");

        ValidatableResponse authorizationUser = userClient.authorization(user, refreshToken);
        int statusCode = authorizationUser.extract().statusCode();
        boolean success = authorizationUser.extract().path("success");
        String message = authorizationUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode);
        Assert.assertEquals(false, success);
        Assert.assertEquals("email or password are incorrect", message);
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void authorizationWthoutLoginNegativeTest() {
        User user = UserGenerator.getRandom();

        ValidatableResponse createUser = userClient.createUser(user);

        accessToken = createUser.extract().path("accessToken");
        String refreshToken = createUser.extract().path("refreshToken");

        user.setEmail("");

        ValidatableResponse authorizationUser = userClient.authorization(user, refreshToken);
        int statusCode2 = authorizationUser.extract().statusCode();

        boolean success = authorizationUser.extract().path("success");
        String message = authorizationUser.extract().path("message");

        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode2);
        Assert.assertEquals(false, success);
        Assert.assertEquals("email or password are incorrect", message);
    }
}