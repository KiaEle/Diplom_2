import base.BaseTest;
import client.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import model.Order;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class ReceivingOrdersFromSpecificUserTest extends BaseTest {

    @Test
    @DisplayName("Получение заказов конкретного пользователя авторизованный пользователь")
    public void changingOrderAuthorizationAndIngredientsPositiveTest() {
        List<String> name = new ArrayList<>();
        name.add("61c0c5a71d1f82001bdaaa6d");
        name.add("61c0c5a71d1f82001bdaaa6f");

        User user = UserGenerator.getRandom();
        Order order = new Order(name);

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        userClient.authorization(user, accessToken);

        ValidatableResponse createOrder = userClient.createOrder(order, accessToken);
        int statusCode = createOrder.extract().statusCode();

        ValidatableResponse getOrder = userClient.getOrderUser(accessToken);
        int statusCode2 = getOrder.extract().statusCode();
        boolean success = getOrder.extract().path("success");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertEquals(HTTP_OK, statusCode2);
        Assert.assertTrue(success);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя неавторизованный пользователь")
    public void changingOrderNotAuthorizationAndIngredientsPositiveTest() {
        List<String> name = new ArrayList<>();
        name.add("61c0c5a71d1f82001bdaaa6d");
        name.add("61c0c5a71d1f82001bdaaa6f");

        User user = UserGenerator.getRandom();
        Order order = new Order(name);

        ValidatableResponse createUser = userClient.createUser(user);
        accessToken = createUser.extract().path("accessToken");

        ValidatableResponse createOrder = userClient.createOrder(order);
        int statusCode = createOrder.extract().statusCode();

        ValidatableResponse getOrder = userClient.getOrderUser();
        int statusCode2 = getOrder.extract().statusCode();
        boolean success = getOrder.extract().path("success");
        String message = getOrder.extract().path("message");

        Assert.assertEquals(HTTP_OK, statusCode);
        Assert.assertEquals(HTTP_UNAUTHORIZED, statusCode2);
        Assert.assertFalse(success);
        Assert.assertEquals("You should be authorised", message);
    }

}